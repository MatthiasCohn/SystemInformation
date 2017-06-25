'
'Credit:
'http://www.codefieber.de/programmierung-1/installierte-software-aus-registry-auslesen-vbs-1367#comments
'
'  Dieses Skript listet alle installierten Programme und Hotfixes in einer Textdatei auf
'  
strHost = "."
 
Const HKLM = &H80000002
Set objReg = GetObject("winmgmts://" & strHost & _
    "/root/default:StdRegProv")
Const strBaseKey = _
    "Software\Microsoft\Windows\CurrentVersion\Uninstall\" 
objReg.EnumKey HKLM,strBaseKey,arrSubKeys
      
For Each strSubKey In arrSubKeys
    intRet = objReg.GetStringValue(HKLM,strBaseKey & strSubKey,_
        "DisplayName",strValue)
    If intRet <> 0 Then 
        intRet = objReg.GetStringValue(HKLM,strBaseKey & strSubKey,_
        "QuietDisplayName",strValue)
    End If
    If (strValue <> "") and (intRet = 0) Then
        set fs = CreateObject("Scripting.FileSystemObject")
        set handle = fs.OpenTextFile(WScript.Arguments.Item(0),8,true)
 
        softwareName = strValue
        handle.WriteLine softwareName
        handle.close
End If
Next