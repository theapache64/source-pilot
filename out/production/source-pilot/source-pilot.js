if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'source-pilot'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'source-pilot'.");
}
this['source-pilot'] = function (_, Kotlin) {
  'use strict';
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  function main() {
    println('Hello World');
  }
  _.main = main;
  main();
  Kotlin.defineModule('source-pilot', _);
  return _;
}(typeof this['source-pilot'] === 'undefined' ? {} : this['source-pilot'], kotlin);
