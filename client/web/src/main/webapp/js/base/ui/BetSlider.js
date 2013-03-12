"use strict";
var Poker = Poker || {};

/**
 * BetSlider used to select the bet/raise amount
 * When a new value is set by this slider it will be stored in Poker.MyPlayer.betAmount
 *
 * You have to set minBet and maxBet. As default start value of the slider is 0
 * you can however not select a value less then minBet, it starts at 0 to give the
 * user an idea how big the min bet is
 *
 * You can add new Markers to the slider by calling Poker.BetSlider.addMarker.
 * When adding a marker the slider will check if there's a value close
 * to the new value you're adding if there is it will not be added to prevent
 * overlapping labels. How close they can be can be set by changing
 * Poker.BetSlider.delta (in percent 0-100)
 *
 * @type {Poker.BetSlider}
 */
Poker.BetSlider = Class.extend({
    minBet : 0,
    maxBet : 0,
    bigBlind : 0,
    delta : 5,
    markers : null,
    slider : null,
    valueOutputs : null,
    containerId : null,
    tableId : 0,
    init : function(tableId,containerId) {
       this.markers = [];
       this.valueOutputs =  $(".slider-value");
       this.containerId = this.containerId + "-" + this.tableId;
       this.tableId = tableId;
    },
    displayOutput : function(value) {
        this.valueOutputs.html("&euro;").append(Poker.Utils.formatCurrency(value));
    },
    /**
     * Draws the bet slider in the container with the id
     * supplied in the constructor, it will remove any existing element with that id
     * and create a new one
     */
    draw : function() {
        var self = this;

        var container = $("#"+this.containerId);
        container.remove();
        container = $("<div/>").attr("id",this.containerId).addClass("poker-slider");

        $("#tableView-"+this.tableId).append(container);

        var sliderMouseDown = function (e) { // disable clicks on track
            var sliderHandle =  self.slider.find('.ui-slider-handle');
            if (e.target != sliderHandle[0]) {
                e.stopImmediatePropagation();
                e.stopPropagation();
                e.preventDefault();
                var val = self.slider.slider("value");

                if(e.target == self.slider.find(".ui-slider-range")[0]) {
                    val = val - self.bigBlind;
                } else {
                    val = val + self.bigBlind;
                }

                self.slider.slider("value",val);
            }
        };

        container.on('mousedown', sliderMouseDown).on('touchstart', sliderMouseDown);

        this.slider = container.
            slider({
                animate: true,
                range: "min",
                orientation: "vertical",
                value: self.minBet,
                max: self.maxBet,
                min: 0,
                step: 50,

                //this gets a live reading of the value and prints it on the page
                slide: function(event,ui ) {
                    self.handleChangeValue(ui.value);
                },

                //this updates the hidden form field so we can submit the data using a form
                change: function(event, ui) {
                    self.handleChangeValue(ui.value);
                }

            });

        $.each(this.markers,function(i,m){
            var value = m.value;
            var marker = m.name;
            var percent = Math.round(100*(value/self.maxBet));


            var div = $("<div/>").append(marker).addClass("marker").css("bottom", percent+"%");
            container.append(div);
            div.touchSafeClick(function(e){
                self.slider.slider("value",value);
            });
        });
        this.displayOutput(this.minBet);
    },
    handleChangeValue : function(value) {
        if(value<this.minBet) {
            this.slider.slider("value",this.minBet);
        } else {
            this.displayOutput(value);
        }
    },
    /**
     * Set min bet value of the slider
     * @param minBet
     */
    setMinBet : function(minBet) {
        this.minBet = minBet;
    },
    /**
     * Set max bet value of the slider, used as the sliders max value
     * @param maxBet
     */
    setMaxBet : function(maxBet){
        this.maxBet = maxBet;
    },
    getValue : function() {
        return this.slider.slider("value");
    },
    setBigBlind : function(bigBlind) {
        this.bigBlind = bigBlind;
    },
    /**
     * Clears the markers of the slider
     */
    clear : function() {
        this.markers = [];
    },
    /**
     * Removes the slider element from the dom
     */
    remove : function() {
      if(this.slider) {
          this.slider.slider("destroy");
          $("#"+this.containerId).remove();
      }
    },
    hide : function() {
        if(this.slider) {
            this.slider.hide();
        }
    },
    closeValueExist : function(val) {
      for(var x in this.markers) {
          var mv = 100 * this.markers[x].value / this.maxBet;
          var valPercent = 100 * val / this.maxBet;
          if(mv<(valPercent+this.delta) && mv>(valPercent-this.delta)){
              return true;
          }
      }
      return false;
    },
    /**
     * Adds a marker to the slider, if there is a marker too close
     * tho this marker value it will be ignored
     *
     * @param name  - marker label
     * @param value - the value of the marker
     */
    addMarker : function(name, value) {
        if(value<=this.maxBet && value>=this.minBet && !this.closeValueExist(value)) {
            this.markers.push({name : name, value  : value})
        }
    }
});
