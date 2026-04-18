$projectRoot = Split-Path -Parent $MyInvocation.MyCommand.Definition
$mvn = Get-Command mvn -ErrorAction SilentlyContinue
if ($mvn) {
    mvn exec:java -Dexec.mainClass=com.sudoku.SudokuApp
    exit $LASTEXITCODE
}

$outDir = Join-Path $projectRoot "out"
if (-Not (Test-Path $outDir)) {
    Write-Host "Build output not found. Running build.ps1..."
    .\build.ps1
}
java -cp $outDir com.sudoku.SudokuApp