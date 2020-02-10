# react-native-laserscanner

## Getting started

`$ npm install @infonl/react-native-laserscanner --save`

## Usage

```javascript
import Scanner from "@infonl/react-native-laserscanner";

(async function () {
  const code = await Scanner.scan();

  await Scanner.stop();
})();
```
