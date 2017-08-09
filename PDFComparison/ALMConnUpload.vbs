
''----------------------------------------------------------------------------------------------------------------------
'' Function Name:    funcGetALMConnectionObj
'' Description:      This will connect to the ALM\QC server via either VbS file or QTP

''###################################################################################################################
Function funcGetALMConnectionObj (sQCServer, sQCUsername, sQCPassword, sQCDomain, sQCProject)

    ''Defining the Parent QC OTA Object
    Set oALMConnObj = CreateObject("TDAPIOLE80.TDConnection")

    ''Initiating the connection to the QC Server
    oALMConnObj.InitConnectionEx sQCServer

    ''Logging in
    oALMConnObj.Login sQCUsername, sQCPassword

    ''Connecting to the required project and domain
    oALMConnObj.Connect sQCDomain, sQCProject

    ''Returning the object
    Set funcGetALMConnectionObj = oALMConnObj

End Function
''###################################################################################################################

''###################################################################################################################
Function DisconnectFromQc()
	'Disconnect from project
	If oALMConnObj.Connected Then
		oALMConnObj.Disconnect()
	End If
	
	'Logout active user
	If oALMConnObj.LoggedIn Then
		oALMConnObj.Logout()
	End If
	
	'Release the QC connection
	oALMConnObj.ReleaseConnection()

	If Not oALMConnObj.Connected Then
		''msgbox "ALM Disconnected"
	End If
	
	Set oALMConnObj=Nothing
End Function
'###################################################################################################################


''Defining the connection parameters for QC

sQCServer = Wscript.arguments(0)
sQCUsername = Wscript.arguments(1)
sQCPassword = Wscript.arguments(2)
sQCDomain = Wscript.arguments(3)
sQCProject = Wscript.arguments(4)


Set oALMConnObj = funcGetALMConnectionObj (sQCServer, sQCUsername, sQCPassword, sQCDomain, sQCProject)


If oALMConnObj.Connected Then

	''msgbox "Connected"
Else
	''msgbox "Not Connected"

End If


strFolderPath = Wscript.arguments(5)

Set TestSetTM=oALMConnObj.TestSetTreeManager 'Create a Tree Manager Object
Set TestFolder=TestSetTM.NodeByPath(strFolderPath)
Set tsList=TestFolder.FindTestSets(Wscript.arguments(6))
''msgbox tsList.count
Set theTestSet = tsList.item(1)
''msgbox theTestSet.name
''msgbox theTestSet.id

Set tfact = oALMConnObj.TestSetFactory
Set tcTreeMgr = oALMConnObj.TreeManager
Set TestSetF = TestFolder.TestSetFactory
Set aTestSetArray = TestSetF.NewList("")

For each cTestSet in aTestSetArray
	If cTestSet.name=Wscript.arguments(6)  Then
	    Set TestCaseF = cTestSet.TSTestFactory 
	    Set aTestCaseArray = TestCaseF.NewList("")
	    tcCnt=aTestCaseArray.count
		For i=1 to tcCnt
			Set  testSet_obj=aTestCaseArray.item(i)
	 	       	tcName=testSet_obj.Test.Name
			'msgbox tcName
			If tcName=Wscript.arguments(7) Then
				runName =testSet_obj.RunFactory.UniqueRunName
				'msgbox runName 
				Set RunF = testSet_obj.RunFactory
				Set theRun = RunF.AddItem(runName)
				theRun.Name =runName
				theRun.Status =Wscript.arguments(8) 
				theRun.CopyDesignSteps
				theRun.Post
				'msgbox theRun.ID
				If Not Wscript.arguments(9) ="" Then
					Set objFoldAttachments = theRun.Attachments
					Set objFoldAttachment = objFoldAttachments.AddItem(Null)
					objFoldAttachment.FileName = Wscript.arguments(9) 
					objFoldAttachment.Type = 1
					objFoldAttachment.Post
					Set objFoldAttachments = Nothing
					Set objFoldAttachment = Nothing
				End If
				
				
				If Not Wscript.arguments(10) ="" Then
					''second attachment
					Set objFoldAttachments = theRun.Attachments				'''second attachment
					Set objFoldAttachment = objFoldAttachments.AddItem(Null)
					'scrshtfolder = CompressResultFolder()
					scrshtfolder = CompressResultFolder(Wscript.arguments(10))
					objFoldAttachment.FileName = scrshtfolder
					objFoldAttachment.Type = 1
					objFoldAttachment.Post
					Set objFoldAttachments = Nothing
					Set objFoldAttachment = Nothing
				End If
				
				
				If Not Wscript.arguments(11) ="" Then
					''second attachment
					Set objFoldAttachments = theRun.Attachments				'''second attachment
					Set objFoldAttachment = objFoldAttachments.AddItem(Null)
					'scrshtfolder = CompressResultFolder()
					scrshtfolder = CompressResultFolder(Wscript.arguments(11))
					objFoldAttachment.FileName = scrshtfolder
					objFoldAttachment.Type = 1
					objFoldAttachment.Post
					Set objFoldAttachments = Nothing
					Set objFoldAttachment = Nothing
				End If
				
				If Not Wscript.arguments(12) ="" Then
					''second attachment
					Set objFoldAttachments = theRun.Attachments				'''second attachment
					Set objFoldAttachment = objFoldAttachments.AddItem(Null)
					'scrshtfolder = CompressResultFolder()
					scrshtfolder = CompressResultFolder(Wscript.arguments(12))
					objFoldAttachment.FileName = scrshtfolder
					objFoldAttachment.Type = 1
					objFoldAttachment.Post
					Set objFoldAttachments = Nothing
					Set objFoldAttachment = Nothing
				End If
				Exit For
			End If
			
		Next
	End If
Next

DisconnectFromQc()

'#####################################################################################################################
'Function Name    		: CompressFolder
'Description     		: Function to compress folder
'Input Parameters 		: folderpath
'Return Value    		: compressed zip folder
'Author				: Cognizant
'Date Created			: 05/08/2008
'#####################################################################################################################
Function CompressResultFolder(strfolderpath)

	Set wshshell = CreateObject("WScript.Shell")
	'Documents = wshShell.SpecialFolders("MyDocuments")
	
	Set objFso=CreateObject("Scripting.FileSystemObject")
		
	'strTmpFoldPath=objFso.GetSpecialFolder(2) & "\"&Environment.Value("TimeStamp")
	'strTmpFoldPath=Wscript.arguments(10)
	 strTmpFoldPath=strfolderpath
	If Not strTmpFoldPath="" Then
		zipfile = strTmpFoldPath&".zip"
		'objFso.CreateFolder strTmpFoldPath
	 
		'folder1 = Environment.Value("ResultPath") & "\" & Environment.Value("TimeStamp")& "\HTML Results"
		folder2 = strTmpFoldPath
		Set objFiSO = CreateObject("Scripting.FileSystemObject")
		objFiSO.OpenTextFile zipfile, 2, True
		Set ShellApp = CreateObject("Shell.Application")
		Set zip = ShellApp.NameSpace(zipfile)
		'zip.CopyHere folder1
		zip.CopyHere folder2
	
		'Set PathFile = objfso.CreateTextFile(objFso.GetSpecialFolder(2) &"\PathFile.txt",True)
	   	'PathFile.Write zipfile
   		'PathFile.Close
		'objFso.DeleteFolder strTmpFoldPath
	
		'''objFso.CopyFolder Environment.Value("ResultPath") & "\" & Environment.Value("TimeStamp")& "\HTML Results",strTmpFoldPath&"\",True
		'''objFso.CopyFolder Environment.Value("ResultPath") & "\" & Environment.Value("TimeStamp")& "\Screenshots",strTmpFoldPath&"\",True
		CompressResultFolder = 	zipfile
	End If
End Function

