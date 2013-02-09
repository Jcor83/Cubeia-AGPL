"use strict";
var Poker = Poker || {};
Poker.DialogManager = Class.extend({
    templateManager : null,
    container : null,
    currentCloseCallback : null,
    open : false,
    dialogQueue : null,
    init : function()  {
        this.dialogQueue = [];
        this.templateManager = new Poker.TemplateManager();
        var c = $("<div/>").attr("id","genericDialogContainer");
        $("body").append(c);
        this.container = $("#genericDialogContainer");

        var self = this;
        $(document).bind("afterClose.facebox",function(){
            if(self.currentCloseCallback!=null) {
                self.currentCloseCallback();
            }
            self.open = false;
            self.openQueuedDialog();
        });
    },
    openQueuedDialog : function() {
        if(this.dialogQueue.length>0) {
            var d = this.dialogQueue[0];
            this.dialogQueue.splice(0,1);
            this.displayDialog(d.dialogId, d.okCallback, d.closeCallback);
        }
    },
    queueDialog : function(dialogId,okCallback,closeCallback) {
        this.dialogQueue.push({
           dialogId : dialogId,
           okCallback : okCallback,
           closeCallback : closeCallback
        });
    },
    /**
     * Displays a generic dialog with a header, message and a continue button
     * example
     * displayManager.displayGenericDialog({header : "header" , message:"message", okButtonText : "reload"});
     * @param {String} content.header - the title
     * @param {String} content.message - the message content
     * @param {String} content.okButtonText - the text of the ok button
     * @param {Function} [okCallback] callback to execute when ok button is clicked
     *
     */
    displayGenericDialog : function(content,okCallback) {
          if(content.header) {
             $("#genericDialog h1").html(content.header);
          } else {
              $("#genericDialog h1").hide();
          }
          if(content.message) {
              $("#genericDialog .message").html(content.message);
          } else {
              $("#genericDialog .message").hide();
          }
         var self = this;
         if(typeof(content.okButtonText)!="undefined") {
            $("#genericDialog .dialog-ok-button").html(content.okButtonText);
         }
         if(typeof(okCallback)=="undefined") {
            this.displayDialog("genericDialog",function(){self.close();},null);
         } else {
             this.displayDialog("genericDialog",function(){ okCallback(); } , null);
         }


    },
    /**
     * Display a dialog by passing a DOM element id you want to be placed in
     * the dialog, if a dialog is open it will be queued and showed when
     * previous dialog is closed
     * @param dialogId
     * @param okCallback
     * @param closeCallback
     */
    displayDialog : function(dialogId, okCallback, closeCallback) {
        if(this.open == true) {
            this.queueDialog(dialogId,okCallback,closeCallback);
            return;
        }
        this.open = true;

        var self = this;
        var fb = $.facebox({div : "#" + dialogId, opacity:0.6});
        if(closeCallback) {
            this.currentCloseCallback = closeCallback;
        }
        $("#facebox .dialog-cancel-button").touchSafeClick(function(){
            self.close();
        });
        $("#facebox .dialog-ok-button").touchSafeClick(function(){
            if(okCallback() || !okCallback) {
                self.close();
            }
        });
    },
    close : function() {
        $.facebox.close();
    }
});
