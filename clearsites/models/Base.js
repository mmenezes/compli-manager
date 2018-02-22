module.exports = function(db) {
	this.db = db;
};
module.exports.prototype = {
	extend: function(properties) {
		var Child = module.exports;
		Child.prototype = module.exports.prototype;
		for(var key in properties) {
			Child.prototype[key] = properties[key];
		}
		return Child;
	},
	setDB: function(db) {
        db = db || "clearsite";
		this.db = db;
	},
	collection: function(collection) {
        collection = collection || "content";
		if(this._collection) return this._collection;
		return this._collection = this.db.collection(collection);
	}
}