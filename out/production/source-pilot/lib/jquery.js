(function (root, factory) {
  if (typeof define === 'function' && define.amd)
    define(['exports', 'kotlin'], factory);
  else if (typeof exports === 'object')
    factory(module.exports, require('kotlin'));
  else {
    if (typeof kotlin === 'undefined') {
      throw new Error("Error loading module 'jquery'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'jquery'.");
    }
    root.jquery = factory(typeof jquery === 'undefined' ? {} : jquery, kotlin);
  }
}(this, function (_, Kotlin) {
  'use strict';
  Kotlin.defineModule('jquery', _);
  return _;
}));

//# sourceMappingURL=jquery.js.map
