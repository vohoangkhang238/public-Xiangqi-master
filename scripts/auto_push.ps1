param(
    [string]$Branch = "main",
    [string]$Remote = "origin",
    [int]$QuietMs = 2000,
    [string]$CommitPrefix = "Auto-save"
)

$root = Split-Path -Parent $MyInvocation.MyCommand.Definition
Set-Location $root

# Debounce timer to coalesce rapid file saves
$timer = New-Object System.Timers.Timer
$timer.Interval = $QuietMs
$timer.AutoReset = $false

$script:pending = $false

$changedFiles = New-Object System.Collections.ArrayList

function Do-CommitAndPush {
    if (-not $changedFiles.Count) { return }
    $dt = Get-Date -Format "yyyy-MM-dd HH:mm:ss"
    $msg = "$CommitPrefix: $dt"
    Write-Host "[auto_push] Staging changes..."
    git add -A
    Write-Host "[auto_push] Committing with message: $msg"
    # Attempt commit; ignore if nothing to commit
    $commit = git commit -m "$msg" 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[auto_push] Pushing to $Remote/$Branch..."
        git push $Remote $Branch
        Write-Host "[auto_push] Push finished." -ForegroundColor Green
    } else {
        Write-Host "[auto_push] Commit skipped or failed: $commit"
    }
    $changedFiles.Clear()
}

$onElapsed = {
    if ($script:pending) {
        $script:pending = $false
        Do-CommitAndPush
    }
}

$timer.add_Elapsed($onElapsed)

$action = {
    $path = $Event.SourceEventArgs.FullPath
    if ($path -match "\\.git\\") { return }
    [void]($changedFiles.Add($path))
    $script:pending = $true
    $timer.Stop()
    $timer.Start()
}

$fsw = New-Object System.IO.FileSystemWatcher $root -Property @{
    IncludeSubdirectories = $true
    EnableRaisingEvents = $true
    NotifyFilter = [System.IO.NotifyFilters]'FileName, LastWrite, LastAccess, Size, DirectoryName'
    Filter = '*.*'
}

Register-ObjectEvent $fsw Changed -Action $action | Out-Null
Register-ObjectEvent $fsw Created -Action $action | Out-Null
Register-ObjectEvent $fsw Renamed -Action $action | Out-Null
Register-ObjectEvent $fsw Deleted -Action $action | Out-Null

Write-Host "[auto_push] Watching: $root" -ForegroundColor Cyan
Write-Host "[auto_push] Branch: $Branch, Remote: $Remote, QuietMs: $QuietMs" -ForegroundColor Cyan
Write-Host "[auto_push] Press Ctrl+C to stop.\n"

try {
    while ($true) { Start-Sleep -Seconds 1 }
} finally {
    Unregister-Event -SourceIdentifier * -ErrorAction SilentlyContinue
    $fsw.Dispose()
    $timer.Dispose()
}
