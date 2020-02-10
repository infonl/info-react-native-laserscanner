# react-native-laserscanner

## Getting started

`$ npm install react-native-laserscanner --save`

### Mostly automatic installation

`$ react-native link react-native-laserscanner`

## Usage

```javascript
import Scanner from "react-native-laserscanner";

// TODO: What to do with the module?
const code = await Scanner.read();

// for cancel
await Scanner.stop();
```
