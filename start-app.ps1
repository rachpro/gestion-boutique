# ============================================
# Démarrage rapide de l'application
# ============================================

$env:CATALINA_HOME = "C:\apache-tomcat"

Write-Host "Démarrage de Tomcat..." -ForegroundColor Cyan
& "$env:CATALINA_HOME\bin\startup.bat" 2>&1 | Out-Null
Start-Sleep -Seconds 15

Write-Host "Ouverture du navigateur..." -ForegroundColor Cyan
Start-Process "http://localhost:8080/gestion-boutique/login"

Write-Host "✅ Application lancée !" -ForegroundColor Green
Write-Host "👉 Identifiant : admin" -ForegroundColor Yellow
Write-Host "👉 Mot de passe : admin123" -ForegroundColor Yellow