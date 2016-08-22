// phantom 1
// var page = require('webpage').create();
// var url = require('system').args[1];
// 
// page.onConsoleMessage = function (message) {
//     console.log(message);
// };
// 
// page.open(url, function (status) {
//     page.evaluate(function(){
//         // Use your namespace instead of `cljs-test-example`:
//         yimp.test.run();
//     });
//     phantom.exit(0);
// });

// phantom 2
var page = require('webpage').create();
var system = require('system');

if (system.args.length !== 2) {
  console.log('Expected a target URL parameter.');
  phantom.exit(1);
}

page.onConsoleMessage = function (message) {
  console.log(message);
};

var url = system.args[1];

page.open(url, function (status) {
  if (status !== "success") {
    console.log('Failed to open ' + url);
    setTimeout(function() { // https://github.com/ariya/phantomjs/issues/12697
      phantom.exit(1);
    }, 0);
  }

  page.evaluate(function() {
   console.log('Testing stuff');
   cljs.yimp.test.run();
  });

  setTimeout(function() { // https://github.com/ariya/phantomjs/issues/12697
    phantom.exit(0);
  }, 0);
});
