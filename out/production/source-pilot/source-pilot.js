if (typeof kotlin === 'undefined') {
  throw new Error("Error loading module 'source-pilot'. Its dependency 'kotlin' was not found. Please, check whether 'kotlin' is loaded prior to 'source-pilot'.");
}
this['source-pilot'] = function (_, Kotlin) {
  'use strict';
  var throwUPAE = Kotlin.throwUPAE;
  var println = Kotlin.kotlin.io.println_s8jyv4$;
  var Unit = Kotlin.kotlin.Unit;
  var endsWith = Kotlin.kotlin.text.endsWith_7epoxm$;
  var first = Kotlin.kotlin.collections.first_2p1efm$;
  var startsWith = Kotlin.kotlin.text.startsWith_7epoxm$;
  var toString = Kotlin.toString;
  var replace = Kotlin.kotlin.text.replace_r2fvfm$;
  var split = Kotlin.kotlin.text.split_ip8yn$;
  var toBoxedChar = Kotlin.toBoxedChar;
  var asList = Kotlin.org.w3c.dom.asList_kt9thq$;
  var throwCCE = Kotlin.throwCCE;
  var ensureNotNull = Kotlin.ensureNotNull;
  var ArrayList_init = Kotlin.kotlin.collections.ArrayList_init_287e2$;
  var Regex_init = Kotlin.kotlin.text.Regex_init_61zpoe$;
  var map = Kotlin.kotlin.sequences.map_z5avom$;
  var toList = Kotlin.kotlin.sequences.toList_veqyi0$;
  var lastOrNull = Kotlin.kotlin.sequences.lastOrNull_veqyi0$;
  var Kind_OBJECT = Kotlin.Kind.OBJECT;
  var fullCode;
  function get_fullCode() {
    if (fullCode == null)
      return throwUPAE('fullCode');
    return fullCode;
  }
  function set_fullCode(fullCode_0) {
    fullCode = fullCode_0;
  }
  var imports;
  function get_imports() {
    if (imports == null)
      return throwUPAE('imports');
    return imports;
  }
  function set_imports(imports_0) {
    imports = imports_0;
  }
  var currentFilePath;
  function get_currentFilePath() {
    if (currentFilePath == null)
      return throwUPAE('currentFilePath');
    return currentFilePath;
  }
  function set_currentFilePath(currentFilePath_0) {
    currentFilePath = currentFilePath_0;
  }
  var isControlActive;
  var activeElement;
  var resLink;
  function checkIsClickable(inputText) {
    var tmp$, tmp$_0;
    println('Checking if ' + inputText + ' is clickable...');
    if (!isLayoutFile(inputText)) {
      if (!get_imports().isEmpty()) {
        var $receiver = get_imports();
        var destination = ArrayList_init();
        var tmp$_1;
        tmp$_1 = $receiver.iterator();
        while (tmp$_1.hasNext()) {
          var element = tmp$_1.next();
          if (endsWith(element, '.' + inputText))
            destination.add_11rb$(element);
        }
        var matchingImports = destination;
        println('Matching imports are');
        println(matchingImports);
        var currentPackageName = Parser_getInstance().currentPackageName_61zpoe$(get_fullCode());
        if (!matchingImports.isEmpty()) {
          tmp$ = first(matchingImports);
        }
         else {
          println('No import matched for ' + inputText + ', setting current name : ' + currentPackageName);
          if (startsWithUppercaseLetter(inputText)) {
            tmp$ = currentPackageName + '.' + inputText;
          }
           else {
            tmp$ = null;
          }
        }
        var matchingImport = tmp$;
        if (matchingImport != null && !startsWith(matchingImport, 'android.') && !startsWith(matchingImport, 'androidx.')) {
          println('Matching import is ' + toString(matchingImport));
          println('Current package name ' + currentPackageName);
          var currentUrl = window.location.toString();
          println('currentUrl is ' + currentUrl);
          var curFileExt = Parser_getInstance().parseFileExt_61zpoe$(currentUrl);
          var other = replace(currentPackageName, 46, 47);
          var packageSlash = String.fromCharCode(47) + other;
          var windowLocSplit = split(currentUrl, [packageSlash]);
          var newUrl = windowLocSplit.get_za3lpa$(0) + String.fromCharCode(toBoxedChar(47)) + replace(matchingImport, 46, 47) + String.fromCharCode(toBoxedChar(46)) + curFileExt + '#L1';
          println('New url is ' + newUrl);
          resLink = newUrl;
          (tmp$_0 = activeElement != null ? activeElement.style : null) != null ? (tmp$_0.textDecoration = 'underline') : null;
          doubleCheckUrl(newUrl);
        }
         else {
          println('No import matched! Matching importing was : ' + toString(matchingImport));
        }
      }
    }
  }
  function isLayoutFile(inputText) {
    return false;
  }
  function doubleCheckUrl$lambda(closure$xhr, closure$newUrl) {
    return function (it) {
      var tmp$;
      if (closure$xhr.status !== 200) {
        println('Invalid URL : ' + closure$newUrl);
        resLink = null;
        (tmp$ = activeElement != null ? activeElement.style : null) != null ? (tmp$.textDecoration = 'none') : null;
      }
       else {
        println('Valid URL it was :)');
      }
      return Unit;
    };
  }
  function doubleCheckUrl(newUrl) {
    var xhr = new XMLHttpRequest();
    xhr.open('GET', newUrl);
    xhr.onload = doubleCheckUrl$lambda(xhr, newUrl);
    xhr.send();
  }
  function removeUnderlineFromActiveElement() {
    var tmp$;
    (tmp$ = activeElement != null ? activeElement.style : null) != null ? (tmp$.textDecoration = 'none') : null;
    activeElement = null;
  }
  function underlineActiveElement() {
    if (activeElement != null) {
      var className = activeElement.innerText;
      if (isControlActive) {
        checkIsClickable(className);
      }
    }
  }
  function main$lambda$lambda(closure$node) {
    return function (it) {
      activeElement = closure$node;
      println('Mouse over...');
      underlineActiveElement();
      return Unit;
    };
  }
  function main$lambda$lambda_0(closure$node) {
    return function (it) {
      println('Mouse left from ' + closure$node.innerText + ', Removing underline...');
      removeUnderlineFromActiveElement();
      return Unit;
    };
  }
  function main$lambda$lambda_1(it) {
    if (isControlActive && resLink != null) {
      window.open(ensureNotNull(resLink), '_blank');
    }
    return Unit;
  }
  function main$lambda(it) {
    if (it.which === 17) {
      isControlActive = true;
      underlineActiveElement();
    }
    return Unit;
  }
  function main$lambda_0(it) {
    if (it.which === 17) {
      isControlActive = false;
      removeUnderlineFromActiveElement();
    }
    return Unit;
  }
  function main() {
    var tmp$, tmp$_0;
    println('Source Pilot Activated (y)');
    set_fullCode((tmp$_0 = (tmp$ = document.querySelector('table.highlight tbody')) != null ? tmp$.textContent : null) != null ? tmp$_0 : '');
    set_imports(Parser_getInstance().parseImports_61zpoe$(get_fullCode()));
    println('Imports are ' + get_imports());
    set_currentFilePath(getCurrentFilePath());
    var allSpan = document.querySelectorAll('table.highlight tbody tr td.blob-code span');
    var tmp$_1;
    tmp$_1 = asList(allSpan).iterator();
    while (tmp$_1.hasNext()) {
      var element = tmp$_1.next();
      var tmp$_2;
      var node = Kotlin.isType(tmp$_2 = element, HTMLSpanElement) ? tmp$_2 : throwCCE();
      node.onmouseover = main$lambda$lambda(node);
      node.onmouseleave = main$lambda$lambda_0(node);
      node.onclick = main$lambda$lambda_1;
    }
    document.onkeydown = main$lambda;
    document.onkeyup = main$lambda_0;
  }
  function getCurrentFilePath() {
    var currentUrl = window.location.toString();
    return Parser_getInstance().getCurrentFilePath_61zpoe$(currentUrl);
  }
  function startsWithUppercaseLetter($receiver) {
    return Regex_init('[A-Z]{1}.*').matches_6bul2c$($receiver);
  }
  function Parser() {
    Parser_instance = this;
    this.IMPORT_PATTERN_0 = Regex_init('import (?<importStatement>[\\w\\.]+)');
    this.PACKAGE_PATTERN_0 = Regex_init('package (?<importStatement>[\\w\\.]+)');
    this.PATH_PATTERN_0 = Regex_init('github\\.com\\/[^\\/]+\\/[^\\/]+\\/(?<path>[^#\\n]+)');
    this.EXT_PATTERN_0 = Regex_init('\\.(\\w+)');
  }
  function Parser$parseImports$lambda(it) {
    var tmp$;
    return ensureNotNull((tmp$ = it.groups.get_za3lpa$(1)) != null ? tmp$.value : null);
  }
  Parser.prototype.parseImports_61zpoe$ = function (fullCode) {
    return toList(map(this.IMPORT_PATTERN_0.findAll_905azu$(fullCode), Parser$parseImports$lambda));
  };
  Parser.prototype.currentPackageName_61zpoe$ = function (fullCode) {
    return ensureNotNull(ensureNotNull(this.PACKAGE_PATTERN_0.find_905azu$(fullCode)).groups.get_za3lpa$(1)).value;
  };
  Parser.prototype.getCurrentFilePath_61zpoe$ = function (fullUrl) {
    return ensureNotNull(ensureNotNull(this.PATH_PATTERN_0.find_905azu$(fullUrl)).groups.get_za3lpa$(1)).value;
  };
  Parser.prototype.parseFileExt_61zpoe$ = function (currentUrl) {
    var lastResult = lastOrNull(this.EXT_PATTERN_0.findAll_905azu$(currentUrl));
    if (lastResult != null) {
      return ensureNotNull(lastResult.groups.get_za3lpa$(1)).value;
    }
    return null;
  };
  Parser.$metadata$ = {
    kind: Kind_OBJECT,
    simpleName: 'Parser',
    interfaces: []
  };
  var Parser_instance = null;
  function Parser_getInstance() {
    if (Parser_instance === null) {
      new Parser();
    }
    return Parser_instance;
  }
  Object.defineProperty(_, 'fullCode', {
    get: get_fullCode,
    set: set_fullCode
  });
  Object.defineProperty(_, 'imports', {
    get: get_imports,
    set: set_imports
  });
  Object.defineProperty(_, 'currentFilePath', {
    get: get_currentFilePath,
    set: set_currentFilePath
  });
  Object.defineProperty(_, 'isControlActive', {
    get: function () {
      return isControlActive;
    },
    set: function (value) {
      isControlActive = value;
    }
  });
  Object.defineProperty(_, 'activeElement', {
    get: function () {
      return activeElement;
    },
    set: function (value) {
      activeElement = value;
    }
  });
  Object.defineProperty(_, 'resLink', {
    get: function () {
      return resLink;
    },
    set: function (value) {
      resLink = value;
    }
  });
  _.checkIsClickable_61zpoe$ = checkIsClickable;
  _.isLayoutFile_61zpoe$ = isLayoutFile;
  _.doubleCheckUrl_61zpoe$ = doubleCheckUrl;
  _.removeUnderlineFromActiveElement = removeUnderlineFromActiveElement;
  _.underlineActiveElement = underlineActiveElement;
  _.main = main;
  _.getCurrentFilePath = getCurrentFilePath;
  var package$extensions = _.extensions || (_.extensions = {});
  package$extensions.startsWithUppercaseLetter_pdl1vz$ = startsWithUppercaseLetter;
  var package$utils = _.utils || (_.utils = {});
  Object.defineProperty(package$utils, 'Parser', {
    get: Parser_getInstance
  });
  isControlActive = false;
  activeElement = null;
  resLink = null;
  main();
  Kotlin.defineModule('source-pilot', _);
  return _;
}(typeof this['source-pilot'] === 'undefined' ? {} : this['source-pilot'], kotlin);
