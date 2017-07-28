# Cognizant Intelligent Test Scripter

## How to Install


### Sikuli OCR support

To enable Sikuli OCR support required Tesseract language files should be added.

Download data files form here,
* [Data file for English v3.02](https://sourceforge.net/projects/tesseract-ocr-alt/files/tesseract-ocr-3.02.eng.tar.gz/download)

* [For other languages](https://github.com/tesseract-ocr/tesseract/wiki/Data-Files)

Copy the data-files from `[datafile]/tesseract-ocr/tessdata` to `Sikulix` data folder.

Create the below folder structure if not present

Windows :   
```
%APPDATA%\Sikulix\SikulixTesseract\tessdata
```

Mac :
```
~/Library/Application Support/Sikulix/SikulixTesseract/tessdata
```

Linux: 

```
~/.Sikulix/SikulixTesseract/tessdata
```

 check out this sikuli [issue](https://answers.launchpad.net/sikuli/+faq/27090) for more details.

### Installing Browser Extensions

Chrome

 * [https://chrome.google.com/webstore/detail/cognizant-intelligent-tes/eghfogfdhadandcigjcccdfhlohcgljn?hl=en-US](https://chrome.google.com/webstore/detail/cognizant-intelligent-tes/eghfogfdhadandcigjcccdfhlohcgljn?hl=en-US)


Firefox

 * Open Firefox
 * Drag and drop the `cognizantits.xpi` which is located in `installation_location/Extensions/FireFox` into `Firefox` Browser
 
Internet Explorer

 * Install the `CognizantITS Toolbar.exe` which is located in `installation_location/Extensions/Internet Explorer`

### Installing Extension Certificate

Open the `Ext_Certificate.cert` file which is in `installation_location/Extensions` . 

For Windows

To install the Certificate, perform the following steps in the Certificate window that is displayed:
 * Click the Install Certificate button

 * Click the Next button and select Place all certificates in the following store.

 * Browse and select Trusted Root Certification Authorities

 * Click the Next button followed by Finish.

For Mac

 * Double-click `Ext_Certificate.cert`. This will bring up the Keychain Access utility. Enter your password to unlock it.

 * Be sure you add the certificate to the System keychain, not the login keychain. Click "Always Trust," even though this doesn't seem to do anything.

 * After it has been added, double-click it. You may have to authenticate again.

 * Expand the "Trust" section.

 * "When using this certificate," set to "Always Trust"

 * Or Open Terminal and then enter 

 ```
 sudo security add-trusted-cert -d -r trustRoot -k /Library/Keychains/System.keychain Ext_Certificate.cert
 ```

## Launching UI

For Windows

Double Click the `Run.bat` to launch the UI

For Mac

Open Terminal in the installation location and then type

`chmod +x Run.command`

Double Click the `Run.command` to launch the UI

If you see **It's Downloaded From Internet Warning** then enter the following command in terminal

`xattr -d -r com.apple.quarantine /path/to/cognizant-intelligent-test-scripter`


