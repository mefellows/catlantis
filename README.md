# Yard Incident Management Portal (YIMP)

## How to run
```
$ npm i
$ lein prod-build
```
Then run in iOS from xcode or `react-native run-ios`

## How to develop
YIMP is based on re-natal 0.2.34+.

```
re-natal use-figwheel
lein figwheel ios
react-native run-ios
```

Please, refer to [re-natal documentation](https://github.com/drapanjanas/re-natal/blob/master/README.md) for more information.

## Building a release package

```
lein prod-build
```

Edit AppDelegate.m and comment out the live reload:

```
//jsCodeLocation = [NSURL URLWithString:@"http://localhost:8081/index.ios.bundle?platform=ios&dev=true"];
 jsCodeLocation = [[NSBundle mainBundle] URLForResource:@"main" withExtension:@"jsbundle"];

```

## Adding an image

Copy image to `./images`. Be sure to add 2x and 3x versions (note the `@2x.png` and `@3x.png` suffixes).
1. Restart React Package Manager
2. Run `re-natil use-figwheel` to update deps
3. `lein figwheel ios` to keep developing

## Publish to App Store

### TestFlight

1. Update `config.cljs` and make sure app pointing to Heroku host.
2. Run fastlane:

```
fastlane beta
```

### App Store

TBC
