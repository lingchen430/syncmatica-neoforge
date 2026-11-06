$ErrorActionPreference = 'Stop'
$targetDir = Join-Path $PSScriptRoot 'libs'
New-Item -ItemType Directory -Force -Path $targetDir | Out-Null
$dependencies = Get-Content -LiteralPath (Join-Path $PSScriptRoot 'dependency-lock.json') -Raw | ConvertFrom-Json
foreach ($dependency in $dependencies) {
    $target = Join-Path $targetDir $dependency.file
    if (-not (Test-Path -LiteralPath $target)) {
        Invoke-WebRequest -Uri $dependency.url -OutFile $target
    }
    $actual = (Get-FileHash -LiteralPath $target -Algorithm SHA256).Hash.ToLowerInvariant()
    if ($actual -ne $dependency.sha256) {
        throw "SHA256 mismatch: $($dependency.file). Remove that file and rerun."
    }
    Write-Output "Verified $($dependency.file)"
}
