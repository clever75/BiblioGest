[Setup]
AppName=BiblioGest
AppVersion=1.0
AppPublisher=Clever
DefaultDirName={autopf}\BiblioGest
DefaultGroupName=BiblioGest
OutputDir=C:\Users\Clever\Desktop\BiblioGest\installer
OutputBaseFilename=BiblioGest_Setup_v1.0
SetupIconFile=C:\Users\Clever\Desktop\BiblioGest\icone.ico
Compression=lzma2
SolidCompression=yes
PrivilegesRequired=admin

[Languages]
Name: "french"; MessagesFile: "compiler:Languages\French.isl"

[Tasks]
Name: "desktopicon"; Description: "Créer un raccourci sur le Bureau"; GroupDescription: "Raccourcis"
Name: "startmenuicon"; Description: "Créer un raccourci dans le Menu Démarrer"; GroupDescription: "Raccourcis"

[Files]
Source: "C:\Users\Clever\Desktop\BiblioGest\output\BiblioGest\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs

[Icons]
Name: "{autodesktop}\BiblioGest"; Filename: "{app}\BiblioGest.exe"; IconFilename: "{app}\BiblioGest.exe"; Tasks: desktopicon
Name: "{group}\BiblioGest"; Filename: "{app}\BiblioGest.exe"; Tasks: startmenuicon
Name: "{group}\Désinstaller BiblioGest"; Filename: "{uninstallexe}"

[Run]
Filename: "{app}\BiblioGest.exe"; Description: "Lancer BiblioGest"; Flags: nowait postinstall skipifsilent