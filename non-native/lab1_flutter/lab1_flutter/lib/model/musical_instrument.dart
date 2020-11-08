import 'dart:ffi';
import 'dart:convert';

class MusicalInstrument {
  int _id;
  String _name;
  String _category;
  String _description;
  int _quantityOnStock;
  double _price;
  String _currency;
  int _isSynchronisedWithServer = 1;

  MusicalInstrument(this._name, this._category, this._description,
      this._quantityOnStock, this._price, this._currency);

  MusicalInstrument.empty();

  MusicalInstrument.map(dynamic obj) {
    this._id = obj['id'];
    this._name = obj['name'];
    this._category = obj['category'];
    this._description = obj['description'];
    this._quantityOnStock = obj['quantityOnStock'];
    this._price = obj['price'];
    this._currency = obj['currency'];
  }

  int get id => _id;

  String get name => _name;

  String get category => _category;

  String get description => _description;

  int get quantityOnStock => _quantityOnStock;

  double get price => _price;

  String get currency => _currency;

  int get isSynchronisedWithServer => _isSynchronisedWithServer;

  void set id(int id) {
    _id = id;
  }

  void set isSynchronisedWithServer(int isSynchronisedWithServer) {
    _isSynchronisedWithServer = isSynchronisedWithServer;
  }

  Map<String, dynamic> toMap() {
    var map = new Map<String, dynamic>();
    if (_id != null) {
      map['id'] = _id;
    }
    map['name'] = _name;
    map['category'] = _category;
    map['description'] = _description;
    map['quantityOnStock'] = _quantityOnStock;
    map['price'] = _price;
    map['currency'] = _currency;

    return map;
  }

  MusicalInstrument.fromMap(Map<String, dynamic> map) {
    this._id = map['id'];
    this._name = map['name'];
    this._category = map['category'];
    this._description = map['description'];
    this._quantityOnStock = map['quantityOnStock'];
    this._price = map['price'];
    this._currency = map['currency'];
  }

  Map<String, dynamic> toJson() => {
        'id': _id.toString(),
        'name': _name,
        'category': _category,
        'description': _description,
        'quantityOnStock': _quantityOnStock.toString(),
        'price': _price.toString(),
        'currency': _currency
      };

  MusicalInstrument.fromJson(Map<String, dynamic> json)
      : _id = json['id'],
        _name = json['name'],
        _category = json['category'],
        _description = json['description'],
        _quantityOnStock = json['quantityOnStock'],
        _price = json['price'],
        _currency = json['currency'];
}
