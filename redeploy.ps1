# ============================================
# Script de redéploiement automatique
# Gestion Boutique - Java EE + PostgreSQL
# ============================================

$ErrorActionPreference = "Continue"

$env:CATALINA_HOME = "C:\apache-tomcat"
$WAR_NAME = "gestion-boutique"

Write-Host ""
Write-Host "============================================" -ForegroundColor Cyan
Write-Host "   REDÉPLOIEMENT GESTION BOUTIQUE" -ForegroundColor Cyan
Write-Host "============================================" -ForegroundColor Cyan
Write-Host ""

# ============================================
# 1. Arrêt de Tomcat
# ============================================
Write-Host "[1/7] Arrêt de Tomcat..." -ForegroundColor Yellow
try {
    & "$env:CATALINA_HOME\bin\shutdown.bat" 2>&1 | Out-Null
} catch {
    Write-Host "      Tomcat n'était pas démarré." -ForegroundColor Gray
}
Start-Sleep -Seconds 5

# ============================================
# 2. Nettoyage des processus Java zombies
# ============================================
Write-Host "[2/7] Nettoyage des processus Java..." -ForegroundColor Yellow
$javaProcesses = Get-Process java -ErrorAction SilentlyContinue
if ($javaProcesses) {
    $javaProcesses | Stop-Process -Force -ErrorAction SilentlyContinue
    Write-Host "      $($javaProcesses.Count) processus Java arrêtés." -ForegroundColor Gray
}
Start-Sleep -Seconds 3

$remainingJava = Get-Process java -ErrorAction SilentlyContinue
if ($remainingJava) {
    Write-Host ""
    Write-Host "   ⚠️ Des processus Java tournent encore :" -ForegroundColor Red
    $remainingJava | Select-Object Id, ProcessName, Path | Format-Table -AutoSize
    Write-Host "   Ferme VS Code / tout autre outil Java, puis relance ce script." -ForegroundColor Red
    exit 1
}

# ============================================
# 3. Suppression de l'ancien déploiement
# ============================================
Write-Host "[3/7] Suppression de l'ancien déploiement..." -ForegroundColor Yellow
Remove-Item "$env:CATALINA_HOME\webapps\$WAR_NAME" -Recurse -Force -ErrorAction SilentlyContinue
Remove-Item "$env:CATALINA_HOME\webapps\$WAR_NAME.war" -Force -ErrorAction SilentlyContinue

# ============================================
# 4. Suppression du dossier target
# ============================================
Write-Host "[4/7] Suppression du dossier target..." -ForegroundColor Yellow
Remove-Item ".\target" -Recurse -Force -ErrorAction SilentlyContinue

if (Test-Path ".\target") {
    Write-Host ""
    Write-Host "   ❌ Impossible de supprimer .\target" -ForegroundColor Red
    Write-Host "   Un processus verrouille encore des fichiers." -ForegroundColor Red
    Write-Host "   Ferme VS Code, l'Explorateur, ou redémarre le PC." -ForegroundColor Red
    exit 1
}

# ============================================
# 5. Compilation Maven
# ============================================
Write-Host "[5/7] Compilation Maven..." -ForegroundColor Yellow
mvn clean package

if ($LASTEXITCODE -ne 0) {
    Write-Host ""
    Write-Host "   ❌ BUILD FAILED" -ForegroundColor Red
    Write-Host "   Corrige les erreurs avant de redéployer." -ForegroundColor Red
    exit 1
}

# Vérification que le WAR a bien été créé
if (-not (Test-Path ".\target\$WAR_NAME.war")) {
    Write-Host ""
    Write-Host "   ❌ Le WAR n'a pas été créé !" -ForegroundColor Red
    Write-Host "   Vérifie que mvn clean package s'est bien terminé." -ForegroundColor Red
    exit 1
}

Write-Host "      ✅ WAR généré : .\target\$WAR_NAME.war" -ForegroundColor Green

# ============================================
# 6. Copie du nouveau WAR
# ============================================
Write-Host "[6/7] Copie du nouveau WAR vers Tomcat..." -ForegroundColor Yellow
Copy-Item ".\target\$WAR_NAME.war" "$env:CATALINA_HOME\webapps\$WAR_NAME.war" -Force

if (-not (Test-Path "$env:CATALINA_HOME\webapps\$WAR_NAME.war")) {
    Write-Host ""
    Write-Host "   ❌ Impossible de copier le WAR vers Tomcat." -ForegroundColor Red
    exit 1
}

Write-Host "      ✅ WAR copié dans Tomcat." -ForegroundColor Green

# ============================================
# 7. Démarrage de Tomcat
# ============================================
Write-Host "[7/7] Démarrage de Tomcat..." -ForegroundColor Yellow
& "$env:CATALINA_HOME\bin\startup.bat" 2>&1 | Out-Null

Write-Host "      Attente du déploiement (15 secondes)..." -ForegroundColor Gray
Start-Sleep -Seconds 15

# Vérification du log Tomcat
$logFile = Get-ChildItem "$env:CATALINA_HOME\logs\catalina.*.log" -ErrorAction SilentlyContinue | 
           Sort-Object LastWriteTime -Descending | 
           Select-Object -First 1

if ($logFile) {
    $logContent = Get-Content $logFile.FullName -Tail 30
    if ($logContent -match "Erreur lors du déploiement|GRAVE") {
        Write-Host ""
        Write-Host "   ⚠️ ERREUR détectée dans le log Tomcat :" -ForegroundColor Red
        $logContent | Select-String -Pattern "Erreur|GRAVE" | Select-Object -First 5
        Write-Host ""
        Write-Host "   Log complet : $($logFile.FullName)" -ForegroundColor Yellow
    } else {
        Write-Host "      ✅ Déploiement OK dans le log Tomcat." -ForegroundColor Green
    }
}

# ============================================
# Résumé
# ============================================
Write-Host ""
Write-Host "============================================" -ForegroundColor Green
Write-Host "   ✅ REDÉPLOIEMENT TERMINÉ" -ForegroundColor Green
Write-Host "============================================" -ForegroundColor Green
Write-Host ""
Write-Host "👉 Application : http://localhost:8080/$WAR_NAME/login" -ForegroundColor Yellow
Write-Host "👉 Identifiant : admin" -ForegroundColor Yellow
Write-Host "👉 Mot de passe : admin123" -ForegroundColor Yellow
Write-Host ""

# Propose d'ouvrir le navigateur
$openBrowser = Read-Host "Ouvrir la page de connexion dans le navigateur ? (O/N)"
if ($openBrowser -eq "O" -or $openBrowser -eq "o") {
    Start-Process "http://localhost:8080/$WAR_NAME/login"
}