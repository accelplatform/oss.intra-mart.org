/*
    json.js
    2006-12-06

    This file adds these methods to JavaScript:

        array.toJSONString()
        boolean.toJSONString()
        date.toJSONString()
        number.toJSONString()
        object.toJSONString()
        string.toJSONString()
            These methods produce a JSON text from a JavaScript value.
            It must not contain any cyclical references. Illegal values
            will be excluded.

            The default conversion for dates is to an ISO string. You can
            add a toJSONString method to any date object to get a different
            representation.

        string.parseJSON(hook)
            This method parses a JSON text to produce an object or
            array. It can throw a SyntaxError exception.

            The optional hook parameter is a function which can filter and
            transform the results. It receives each of the values, and its
            return value is used instead. If it returns what it received, then
            structure is not modified.

            Example:

            // Parse the text. If it contains any "NaN" strings, replace them
            // with the NaN value. All other values are left alone.

            myData = text.parseJSON(function (value) {
                if (value === 'NaN') {
                    return NaN;
                }
                return value;
            });

    It is expected that these methods will formally become part of the
    JavaScript Programming Language in the Fourth Edition of the
    ECMAScript standard in 2007.
*/
if (!Object.prototype.toJSONString) {
    Array.prototype.toJSONString = function () {
        var a = ['['], b, i, l = this.length, v;

        function p(s) {
            if (b) {
                a.push(',');
            }
            a.push(s);
            b = true;
        }

        for (i = 0; i < l; i += 1) {
            v = this[i];
            switch (typeof v) {
            case 'undefined':
                p("\"undefined\"");
                break;
            case 'function':
            case 'unknown':
                break;
            case 'object':
                if (v) {
                    if (typeof v.toJSONString === 'function') {
                        p(v.toJSONString());
                    }
                } else {
                    p("null");
                }
                break;
            default:
                p(v.toJSONString());
            }
        }
        a.push(']');
        return a.join('');
    };

    Boolean.prototype.toJSONString = function () {
        return String(this);
    };

    Date.prototype.toJSONString = function () {
        function f2(n) {
            return parseInt(n,10) < 10 ? '0' + n : n;
        }

        function f4(n) {
            n = parseInt(n,10);
            if(n < 10){
                return '000' + n;
            }
            else if(n < 100){
                return '00' + n;
            }
            else if(n < 1000){
                return '0' + n;
            }
            else{
                return n;
            }
        }

        return '"' +
                 f4(this.getUTCFullYear())     + '-' +
                 f2(this.getUTCMonth() + 1)    + '-' +
                 f2(this.getUTCDate())         + 'T' +
                 f2(this.getUTCHours())        + ':' +
                 f2(this.getUTCMinutes())      + ':' +
                 f2(this.getUTCSeconds())      + ',' +
                 f4(this.getUTCMilliseconds()) +
               '"';
    };

    Number.prototype.toJSONString = function () {
        return isFinite(this) ? String(this) : "\"" + String(this) + "\"";
    };

    Object.prototype.toJSONString = function () {
        var a = ['{'], b, i, v;

        function p(s) {
            if (b) {
                a.push(',');
            }
            a.push(i.toJSONString(), ':', s);
            b = true;
        }

        for (i in this) {
            if (this.hasOwnProperty(i)) {
                v = this[i];
                switch (typeof v) {
                case 'undefined':
                    p("\"undefined\"");
                    break;
                case 'function':
                case 'unknown':
                    break;
                case 'object':
                    if (v) {
                        if (typeof v.toJSONString === 'function') {
                            p(v.toJSONString());
                        }
                    } else {
                        p("null");
                    }
                    break;
                default:
                    p(v.toJSONString());
                }
            }
        }
        a.push('}');
        return a.join('');
    };


    (function (s) {
        var m = {
            '\b': '\\b',
            '\t': '\\t',
            '\n': '\\n',
            '\f': '\\f',
            '\r': '\\r',
            '"' : '\\"',
            '\\': '\\\\'
        };

        s.parseJSON = function (hook) {
            try {
                if (/^("(\\.|[^"\\\n\r])*?"|[,:{}\[\]0-9.\-+Eaeflnr-u \n\r\t])+?$/.
                        test(this)) {
                    var j = eval('(' + this + ')');
                    if (typeof hook === 'function') {
                        function walk(v) {
                            if (v && typeof v === 'object') {
                                for (var i in v) {
                                    if (v.hasOwnProperty(i)) {
                                        v[i] = walk(v[i]);
                                    }
                                }
                            }
                            return hook(v);
                        }
                        return walk(j);
                    }
                    return j;
                }
            } catch (e) {
            }
            throw new SyntaxError("parseJSON");
        };

        s.toJSONString = function () {
            if (/["\\\x00-\x1f]/.test(this)) {
                return '"' + this.replace(/([\x00-\x1f\\"])/g, function(a, b) {
                    var c = m[b];
                    if (c) {
                        return c;
                    }
                    c = b.charCodeAt();
                    return '\\u00' +
                        Math.floor(c / 16).toString(16) +
                        (c % 16).toString(16);
                }) + '"';
            }
            return '"' + this + '"';
        };
    })(String.prototype);
}

/**
 * Function : /intra-mart/jssp-rpc/marshller/function-name/marshall
 */
var marshall = function (obj) {
    if ((typeof obj) === "undefined") {
        return "\"undefined\"";
    } else {
        return obj.toJSONString();
    }
};

/**
 * Function : /intra-mart/jssp-rpc/marshller/function-name/marshall-arguments
 */
var marshall4Arguments = function (args) {
    var ary = new Array();
    for (i = 0; i < args.length; i++) {
        if ((typeof args[i]) === "undefined") {
            ary[i] =  "\"undefined\"";
        } else {
            ary[i] = args[i].toJSONString();
        }
    }
    return "[" + ary.join(",") + "]";
};

/**
 * Function : /intra-mart/jssp-rpc/marshller/function-name/unmarshall
 */
var unmarshall = function (jsonString) {
    return jsonString.parseJSON(
            function (value) {
                if (value === "undefined") {
                    return undefined;
                }
                else if (value === "NaN") {
                    return NaN;
                }
                else if (value === "Infinity") {
                    return Infinity;
                }
                else if (value === "-Infinity") {
                    return -Infinity;
                }
                else if (/^[0-9]{4}-[0-9]{2}-[0-9]{2}T[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{4}$/.test(value)) {
                    var yyyy   = parseInt(value.substring( 0, 4), 10);
                    var MM     = parseInt(value.substring( 5, 7), 10);
                    var dd     = parseInt(value.substring( 8,10), 10);
                    var hh     = parseInt(value.substring(11,13), 10);
                    var mm     = parseInt(value.substring(14,16), 10);
                    var ss     = parseInt(value.substring(17,19), 10);
                    var millis = parseInt(value.substring(20)   , 10);

                    var d = new Date();

                    d.setUTCFullYear(yyyy);
                    d.setUTCMonth(MM - 1);
                    d.setUTCDate(dd);
                    d.setUTCHours(hh);
                    d.setUTCMinutes(mm);
                    d.setUTCSeconds(ss);
                    d.setUTCMilliseconds(millis);

                    return d;
                } else {
                    return value;
                }
            }
    );
};
