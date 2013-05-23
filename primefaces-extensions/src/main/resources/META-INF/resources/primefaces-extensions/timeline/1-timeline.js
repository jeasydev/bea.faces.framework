/**
 * PrimeFaces Extensions Timeline Widget.
 *
 * @author Oleg Varaksin, reimplemented since 0.7
 */
PrimeFacesExt.widget.Timeline = PrimeFaces.widget.BaseWidget.extend({
    /**
     * Initializes the widget.
     *
     * @param {Object} cfg widget configuration's object.
     */
    init: function (cfg) {
        this._super(cfg);
        this.cfg = cfg;
        this.id = cfg.id;

        PrimeFacesExt.handleInitialize(this, this.createTimeline);
    },

    /**
     * Creates timeline widget with all initialization steps.
     */
    createTimeline: function () {
        // configure localized text
        this.cfg.opts = PrimeFacesExt.configureLocale('Timeline', this.cfg.opts);

        // instantiate a timeline object
        this.instance = new links.Timeline(document.getElementById(this.id));

        // draw the timeline with created data and options
        this.instance.draw(this.cfg.data, this.cfg.opts);

        // bind events
        this.bindEvents();
    },

    /**
     * Binds timeline's events.
     */
    bindEvents: function () {
        if (this.cfg.opts.responsive) {
            var nsevent = "resize.timeline" + PrimeFaces.escapeClientId(this.id);
            $(window).off(nsevent).on(nsevent, $.proxy(function () {
                this.instance.checkResize();
            }, this));
        }

        // "select" event
        if (this.cfg.opts.selectable && this.getBehavior("select")) {
            links.events.addListener(this.instance, 'select', $.proxy(function () {
                var index = this.getSelectedIndex();
                if (index < 0) {
                    return;
                }
                
                var ext = {
                    params: [
                        {name: this.id + '_eventIdx', value: index}
                    ]
                };

                this.getBehavior("select").call(this, null, ext);
            }, this));
        }

        // "add" event
        if (this.cfg.opts.selectable && this.cfg.opts.editable && this.getBehavior("add")) {
            links.events.addListener(this.instance, 'add', $.proxy(function () {
                var event = this.getSelectedEvent();
                if (event == null) {
                    return;
                }
                
                var params = [];
                params.push({
                    name: this.id + '_startDate',
                    value: event.start.getTime()
                });
                
                if (event.end) {
                    params.push({
                        name: this.id + '_endDate',
                        value: event.end.getTime()
                    });    
                }
                
                if (event.group) {
                    params.push({
                        name: this.id + '_group',
                        value: event.group
                    });    
                }

                this.getBehavior("add").call(this, null, {params: params});
            }, this));
        }
        
        // "change" event
        if (this.cfg.opts.selectable && this.cfg.opts.editable && this.getBehavior("change")) {
            links.events.addListener(this.instance, 'change', $.proxy(function () {
                var index = this.getSelectedIndex();
                if (index < 0) {
                    return;
                }
                
                var event = this.getEvent(index);
                if (event == null) {
                    return;
                }
                
                if (!this.instance.isEditable(event)) {
                    // only editable events can be changed
                    return;
                }
                
                var params = [];
                params.push({
                    name: this.id + '_eventIdx',
                    value: index
                });
                
                params.push({
                    name: this.id + '_startDate',
                    value: event.start.getTime()
                });
                
                if (event.end) {
                    params.push({
                        name: this.id + '_endDate',
                        value: event.end.getTime()
                    });    
                }
                
                if (event.group) {
                    params.push({
                        name: this.id + '_group',
                        value: event.group
                    });    
                }

                this.getBehavior("change").call(this, null, {params: params});
            }, this));
        }
        
        // "edit" event
        if (this.cfg.opts.selectable && this.cfg.opts.editable && this.getBehavior("edit")) {
            links.events.addListener(this.instance, 'edit', $.proxy(function () {
                var index = this.getSelectedIndex();
                if (index < 0) {
                    return;
                }
                
                if (!this.isEditable(index)) {
                    // only editable events can be edited
                    return;
                }
                
                var ext = {
                    params: [
                        {name: this.id + '_eventIdx', value: index}
                    ]
                };

                this.getBehavior("edit").call(this, null, ext);
            }, this));
        }
        
        // "delete" event
        if (this.cfg.opts.selectable && this.cfg.opts.editable && this.getBehavior("delete")) {
            links.events.addListener(this.instance, 'delete', $.proxy(function () {
                var index = this.getSelectedIndex();
                if (index < 0) {
                    return;
                }
                
                if (!this.isEditable(index)) {
                    // only editable events can be deleted
                    return;
                }
                
                var ext = {
                    params: [
                        {name: this.id + '_eventIdx', value: index}
                    ]
                };

                this.getBehavior("delete").call(this, null, ext);
            }, this));
        }
        
        // "rangechange" event
        if ((this.cfg.opts.zoomable || this.cfg.opts.moveable) && this.getBehavior("rangechange")) {
            links.events.addListener(this.instance, 'rangechange', $.proxy(function () {
                var range = this.instance.getVisibleChartRange();
                
                var ext = {
                    params: [
                        {name: this.id + '_startDate', value: range.start.getTime()},
                        {name: this.id + '_endDate', value: range.end.getTime()}
                    ]
                };

                this.getBehavior("rangechange").call(this, null, ext);
            }, this));
        }
        
        // "rangechanged" event
        if ((this.cfg.opts.zoomable || this.cfg.opts.moveable) && this.getBehavior("rangechanged")) {
            links.events.addListener(this.instance, 'rangechanged', $.proxy(function () {
                var range = this.instance.getVisibleChartRange();
                
                var ext = {
                    params: [
                        {name: this.id + '_startDate', value: range.start.getTime()},
                        {name: this.id + '_endDate', value: range.end.getTime()}
                    ]
                };

                this.getBehavior("rangechanged").call(this, null, ext);
            }, this));
        }
    },
    
    /**
     * Retrieves the array of current data (events) as an JSON string. This method is useful when you done some changes
     * in timeline and want to send them to server to update the backing model (with pe:remoteCommand and pe:convertTimelineEvents).
     * 
     * @return {Object}
     */
    getData: function () {
        var newData = $.map(this.instance.getData(), $.proxy(function(item) {
            var newItem = {};
            if (item.hasOwnProperty('content')) {
                newItem.data = item.content;
            }
            
            if (item.hasOwnProperty('start')) {
                newItem.startDate = item.start.getTime();
            }
            
            if (item.hasOwnProperty('end') && (item.end != null)) {
                newItem.endDate = item.end.getTime();
            } else {
                newItem.endDate = null;
            }
            
            if (item.hasOwnProperty('editable')) {
                newItem.editable = item.editable;
            } else {
                newItem.editable = null;
            }
            
            if (item.hasOwnProperty('group')) {
                newItem.group = item.group;
            } else {
                newItem.group = null;
            }
            
            if (item.hasOwnProperty('className')) {
                newItem.styleClass = item.className;
            } else {
                newItem.styleClass = null;
            }
            
            return newItem;
        }, this));
        
        return JSON.stringify(newData);
    },
    
    /**
     * Adds an event to the timeline. The provided parameter properties is an object, containing parameters
     * "start" (Date), "end" (Date), "content" (String), "group" (String). Parameters "end" and "group" are optional. 
     * 
     * @param properties event's properties
     */
    addEvent: function (properties) {
        this.instance.addItem(properties);
    },
    
    /**
     * Changes properties of an existing item in the timeline. The provided parameter properties is an object,
     * and can contain parameters "start" (Date), "end" (Date), "content" (String), "group" (String).
     * 
     * @param index index of the event. 
     * @param properties event's properties
     */
    changeEvent: function (index, properties) {
        this.instance.changeItem(index, properties);
    },
    
    /**
     * Deletes an existing event. 
     * 
     * @param index index of the event. 
     * @param preventRender optional boolean parameter to prevent re-render timeline immediately after delete
     *        (for multiple deletions). Default is false.
     */
    deleteEvent: function (index, preventRender) {
        this.instance.deleteItem(index, preventRender);
    },
    
    /**
     * Deletes all events from the timeline.
     */
    deleteAllEvents: function () {
        this.instance.deleteAllItems();
    },
    
    /**
     * Cancels event adding.
     */
    cancelAdd: function () {
        this.instance.cancelAdd();
    },
    
    /**
     * Cancels event changing.
     */
    cancelChange: function () {
        this.instance.cancelChange();
    },
    
    /**
     * Cancels event deleting.
     */
    cancelDelete: function () {
        this.instance.cancelDelete();
    },
    
    /**
     * Retrieves the properties of a single event. The returned object can contain parameters
     * "start" (Date), "end" (Date), "content" (String), "group" (String).
     * 
     * @return {Object} 
     */
    getEvent: function (index) {
        return this.instance.getItem(index);
    },

    /**
     * Is the event by given index editable?
     * 
     * @param index index of the event
     * @return {boolean} true - editable, false - not
     */
    isEditable: function(index) {
        return this.instance.isEditable(this.getEvent(index));
    },
    
    /**
     * Returns an object with start and end properties, which each one of them is a Date object,
     * representing the currently visible time range.
     * 
     * @return {Object}
     */
    getVisibleRange: function () {
        return this.instance.getVisibleChartRange();
    },
    
    /**
     * Sets the visible range (zoom) to the specified range. Accepts two parameters of type Date
     * that represent the first and last times of the wanted selected visible range.
     * Set start to null to include everything from the earliest date to end;
     * set end to null to include everything from start to the last date.
     *
     * @param start start Date
     * @param end end Date 
     */
    setVisibleRange: function (start, end) {
        return this.instance.setVisibleChartRange(start, end);
    },
    
    /**
     * Check if the timeline container is resized, and if so, resize the timeline. Useful when the webpage is resized.
     */
    checkResize: function () {
        this.instance.checkResize();
    },
    
    /**
     * Gets number of events (items in the timeline).
     * 
     * @return {Integer} 
     */
    getNumberOfEvents: function () {
        return this.instance.getData().length;
    },    

    /**
     * Gets behavior callback by name or null.
     * 
     * @param name behavior name
     * @return {Function}
     */
    getBehavior: function (name) {
        return this.cfg.behaviors ? this.cfg.behaviors[name] : null;
    },

    /**
     * Gets index of the currently selected event or -1.
     * 
     * @return {Number}
     */
    getSelectedIndex: function () {
        var selection = this.instance.getSelection();
        if (selection.length) {
            if (selection[0].row != undefined) {
                return selection[0].row;
            }
        }

        return -1;
    },

    /**
     * Gets currently selected event with properties or null. Properties are
     * "start" (Date), "end" (Date), "content" (String), "group" (String), "editable" (boolean).
     * 
     * @return {Object} JSON object
     */
    getSelectedEvent: function () {
        var index = this.getSelectedIndex();
        if (index != -1) {
            return this.instance.getItem(index);
        }

        return null;
    },

    /**
     * Selects an event by index. The visible range will be moved,
     * so that the selected event is placed in the middle.
     * 
     * @param index
     */
    setSelection: function(index) {
        this.instance.setSelection([{
            'row': index
        }]);    
    },

    /**
     * Gets instance of the Timeline object.
     * 
     * @return {Object}
     */
    getInstance: function () {
        return this.instance;
    }
});