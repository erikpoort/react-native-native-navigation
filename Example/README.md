# Example
This example project is running on a local dependency to the library.  
The package.json will show "file:..", where you would normally use "react-native-native-navigation"

## Running
To succesfully run the example, you need to start the packager with a custom configuration first. This is already setup in the package.json, simply open a terminal window, cd to Example and call:  
```
$ npm run start
```

After this you can use `react-native run-ios` or use Xcode / Android Studio to run the app.

## Troubleshooting
If you get the following error:
`Unable to resolve module 'react' ...`  
Close any running packager and follow the steps above.