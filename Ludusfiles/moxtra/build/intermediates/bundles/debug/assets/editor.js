var mx_editor = {};
var currentTag;
var commands = ["insertUnorderedList", "insertOrderedList", 
                "bold", "italic", "strikeThrough", "underline", 
                "foreColor", "hiliteColor", 
                "insertHorizontalRule", 
                "justifyLeft", "justifyCenter", "justifyRight", "justifyFull", 
                "indent", "outdent"];
var ypos;

function getSelectedNode() {
    var node,selection;
    if (window.getSelection) {
        selection = getSelection();
        node = selection.anchorNode;
    }
    if (!node && document.selection) {
        selection = document.selection;
        var range = selection.getRangeAt ? selection.getRangeAt(0) : selection.createRange();
        node = range.commonAncestorContainer ? range.commonAncestorContainer :
        range.parentElement ? range.parentElement() : range.item(0);
    }
    if (node) {
        return (node.nodeName == "#text" ? node.parentNode : node);
    }
}

function getCaretCharacterOffsetWithin(element) {
    var caretOffset = 0;
    var doc = element.ownerDocument || element.document;
    var win = doc.defaultView || doc.parentWindow;
    var sel;
    if (typeof win.getSelection != "undefined") {
        sel = win.getSelection();
        if (sel.rangeCount > 0) {
            var range = win.getSelection().getRangeAt(0);
            var preCaretRange = range.cloneRange();
            preCaretRange.selectNodeContents(element);
            preCaretRange.setEnd(range.endContainer, range.endOffset);
            caretOffset = preCaretRange.toString().length;
        }
    } else if ( (sel = doc.selection) && sel.type != "Control") {
        var textRange = sel.createRange();
        var preCaretTextRange = doc.body.createTextRange();
        preCaretTextRange.moveToElementText(element);
        preCaretTextRange.setEndPoint("EndToEnd", textRange);
        caretOffset = preCaretTextRange.text.length;
    }
    return caretOffset;
}

function updateButtonStatus() {
    var enabledCommands = "";
    for (var i = 0; i < commands.length; i++) {
        var cmd = commands[i];
        if (document.queryCommandState(cmd)) {
            if (enabledCommands !== "") {
                enabledCommands += "," + cmd;
            } else {
                enabledCommands = cmd;
            }
        }
    }
    var formatBlock = document.queryCommandValue("formatBlock");
    if (formatBlock.length > 0) {
        enabledCommands += "," + formatBlock;
    }
    window.HtmlEditor.updateButtonStatus(enabledCommands, getCaretCharacterOffsetWithin(document.documentElement));
}

function setEndOfContentEditable(contentEditableElement)
{
    var range,selection;
    if(document.createRange) {
        range = document.createRange();
        range.selectNodeContents(contentEditableElement);
        range.collapse(false);
        selection = window.getSelection();
        selection.removeAllRanges();
        selection.addRange(range);
    }
}

mx_editor.init = function() {
    document.body.contentEditable=true;

    document.onselectionchange = function() {
        ypos = window.pageYOffset;
        updateButtonStatus();
    };

    document.ontouchend = function() {
        ypos = window.pageYOffset;
        updateButtonStatus();
    };

    window.addEventListener('resize', function() {
        window.scrollTo(0, ypos);
    });

    window.HtmlEditor.showIME(true);

    setEndOfContentEditable(document.documentElement);
};

function toggleHeading(tag) {
    var currentNode = getSelectedNode();
    if (currentNode.tagName.toLowerCase() === tag) {
        document.execCommand('removeFormat', false, null);
        document.execCommand('formatBlock', false, "p");
    } else {
        document.execCommand('formatBlock', false, tag);
    }
}

mx_editor.execCommand = function(cmd, value) {
    if (cmd === 'h1' || cmd === 'h2') {
        toggleHeading(cmd);
    } else {
        value = typeof value !== 'undefined' ? value : null;
        document.execCommand(cmd, false, value);
    }
    ypos = window.pageYOffset;
    updateButtonStatus();
    window.HtmlEditor.showIME(false);
};

