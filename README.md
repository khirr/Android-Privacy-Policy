# Android-Privacy-Policy
A simple Android library to show Privacy Policy, Terms of Service and General Data Protection Regulation (GDPR).

Api-level 16+

Preview:

<img src="https://github.com/khirr/Android-Privacy-Policy/blob/master/art/main-view-screenshot.png" width="480">

Usage:

Add the repository to your gradle app
You can use 1.0.1 for AppCompat project, use 1.0.2 or higher for AndroidX projects.
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}

dependencies {
    implementation 'com.github.khirr:Android-Privacy-Policy:1.0.1'
}
```

Add this view to your code
```
// Initialice your dialog, first param is your terms of service url, and second param is your privacy policy url

PrivacyPolicyDialog dialog = new PrivacyPolicyDialog(this,
                "https://localhost/terms",
                "https://localhost/privacy");
```

Add your policy lines, the most import for users, your full policy and terms of service are on your urls.
```
dialog.addPoliceLine("This application uses a unique user identifier for advertising purposes, it is shared with third-party companies.");
dialog.addPoliceLine("This application sends error reports, installation and send it to a server of the Fabric.io company to analyze and process it.");
dialog.addPoliceLine("This application requires internet access and must collect the following information: Installed applications and history of installed applications, ip address, unique installation id, token to send notifications, version of the application, time zone and information about the language of the device.");
dialog.addPoliceLine("All details about the use of data are available in our Privacy Policies, as well as all Terms of Service links below.");
```

Add your listener
```
final Intent intent = new Intent(this, SecondActivity.class);

dialog.setOnClickListener(new PrivacyPolicyDialog.OnClickListener() {
    @Override
    public void onAccept(boolean isFirstTime) {
        Log.e("MainActivity", "Policies accepted");
        startActivity(intent);
        finish();
    }

    @Override
    public void onCancel() {
        Log.e("MainActivity", "Policies not accepted");
        finish();
    }
});
```
Enable for Europe only
```
dialog.setEuropeOnly(true);
```

Full dialog customizing
```
//  Example
dialog.setTitleTextColor(Color.parseColor("#222222"));
dialog.setAcceptButtonColor(ContextCompat.getColor(this, R.color.colorAccent));
```

## License
MIT
