Android-Features
================

Features is a utility class for Android that helps you manage the features of your app.

## How it works

Features loads and parses .properties files placed in **res/raw** of your application (it loads a debug file if debuggable is true), then uses a singleton pattern to front a hasFeature method that allows you to check if a feature is enabled or not.

## Implementation

First create a folder in your res folder named raw, then place two files there, one for the live build and one for the debug/dev (I usually name them features_app_debug.properties and features_app.properties)

Then open the files and specify the features using the following format (we parse them with java.util.Properties).

    YOURFEATURE1 = 0
    YOURFEATURE2 = 1


After that you need to include the utility and run createInstance when your app starts, I recommend you place it in application onCreate, like this:


    public class YourApplication extends Application {
        @Override
        public void onCreate() {
            Features.createInstance().load(this,
                R.raw.features_app_debug,
                R.raw.features_app);
        }
    }

You can also skip the validation part and load the features file directly.

    public class YourApplication extends Application {
        @Override
        public void onCreate() {
            Features.createInstance().load(this, R.raw.features_app_debug);
        }
    }




Finally implement the feature checking by running something like this:

    if (Features.getInstance().hasFeature("YOURFEATURE")) {
        // Feature is enabled, do stuff!
    }

## Contributing

Want to contribute? Awesome. Just send a pull request.


## License

Android-Features is released under the [MIT License](http://www.opensource.org/licenses/MIT).
