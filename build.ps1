$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$mvn = Get-Command mvn -ErrorAction SilentlyContinue

if ($mvn) {
    Write-Host "Maven found. Running mvn clean test..."
    mvn clean test
    exit $LASTEXITCODE
}

Write-Host "Maven not found. Falling back to direct javac compilation..."
$sourceDir = Join-Path $projectRoot "src\main\java"
$testDir = Join-Path $projectRoot "src\test\java"
$outDir = Join-Path $projectRoot "out"

if (-Not (Test-Path $outDir)) {
    New-Item -ItemType Directory -Path $outDir | Out-Null
}

$sourceFiles = Get-ChildItem -Path $sourceDir -Recurse -Filter *.java | Select-Object -ExpandProperty FullName

javac -d $outDir $sourceFiles
if ($LASTEXITCODE -ne 0) {
    Write-Error "Compilation failed."
    exit $LASTEXITCODE
}

Write-Host "Compilation successful. Maven is not installed, so tests are skipped in fallback mode."

Write-Host "Build successful. Tests are skipped in fallback mode."
