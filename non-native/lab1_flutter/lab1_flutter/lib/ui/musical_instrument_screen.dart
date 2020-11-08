import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:lab1_flutter/model/musical_instrument.dart';
import 'package:lab1_flutter/ui/listview_musical_instrument.dart';
import 'package:lab1_flutter/util/musical_instruments_database_helper.dart';
import 'package:lab1_flutter/util/network_repository.dart';

class MusicalInstrumentScreen extends StatefulWidget {
  final MusicalInstrument musicalInstrument;
  MusicalInstrumentScreen(this.musicalInstrument);

  @override
  State<StatefulWidget> createState() => new _MusicalInstrumentScreenState();
}

class _MusicalInstrumentScreenState extends State<MusicalInstrumentScreen> {
  MusicalInstrumentsDatabaseHelper db = new MusicalInstrumentsDatabaseHelper();
  NetworkRepository networkRepository = new NetworkRepository();

  TextEditingController _nameController;
  TextEditingController _categoryController;
  TextEditingController _descriptionController;
  TextEditingController _quantityOnStockController;
  TextEditingController _priceController;
  TextEditingController _currencyController;

  @override
  void initState() {
    super.initState();

    _nameController = new TextEditingController(text: widget.musicalInstrument.name);
    _categoryController = new TextEditingController(text: widget.musicalInstrument.category);
    _descriptionController = new TextEditingController(text: widget.musicalInstrument.description);
    _quantityOnStockController = new TextEditingController(text: widget.musicalInstrument.quantityOnStock.toString());
    _priceController = new TextEditingController(text: widget.musicalInstrument.price.toString());
    _currencyController = new TextEditingController(text: widget.musicalInstrument.currency);

  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Musical Instrument'),
        backgroundColor: Colors.myColor,
      ),
      body: Container(
        margin: EdgeInsets.all(15.0),
        alignment: Alignment.center,
        child: ListView(
          children: <Widget>[
            TextField(
              controller: _nameController,
              decoration: InputDecoration(labelText: 'Name...'),
            ),
            Padding(padding: new EdgeInsets.all(5.0)),
            TextField(
              controller: _categoryController,
              decoration: InputDecoration(labelText: 'Category...'),
            ),
            Padding(padding: new EdgeInsets.all(5.0)),
            TextField(
              controller: _descriptionController,
              decoration: InputDecoration(labelText: 'Description...'),
            ),
            Padding(padding: new EdgeInsets.all(5.0)),
            TextField(
              controller: _quantityOnStockController,
              keyboardType: TextInputType.numberWithOptions(signed: true, decimal: false),
              inputFormatters: [WhitelistingTextInputFormatter.digitsOnly],
              decoration: InputDecoration(labelText: 'Quantity on stock...'),
            ),
            Padding(padding: new EdgeInsets.all(5.0)),
            TextField(
              controller: _priceController,
              keyboardType: TextInputType.numberWithOptions(signed: false, decimal: true),
              decoration: InputDecoration(labelText: 'Price...'),
            ),
            Padding(padding: new EdgeInsets.all(5.0)),
            TextField(
              controller: _currencyController,
              decoration: InputDecoration(labelText: 'Currency...'),
            ),
            Padding(padding: new EdgeInsets.all(5.0)),
            RaisedButton(
              child: (widget.musicalInstrument.id != null) ? Text('Update') : Text('Add'),
              onPressed: () async{
                if(_nameController.text.isEmpty || _categoryController.text.isEmpty || _descriptionController.text.isEmpty ||
                _priceController.text.isEmpty || _quantityOnStockController.text.isEmpty || _currencyController.text.isEmpty){
                  MusicalInstrument m1 = new MusicalInstrument('', '', '', 1, 1, '');
                  m1.id = null;
                  Navigator.pop(context, m1);
                } else {
                  if (widget.musicalInstrument.id != null) {
                    MusicalInstrument m1 = MusicalInstrument.fromMap({
                      'id': widget.musicalInstrument.id,
                      'name': _nameController.text,
                      'category': _categoryController.text,
                      'description': _descriptionController.text,
                      'quantityOnStock': int.parse(_quantityOnStockController.text),
                      'price': double.parse(_priceController.text),
                      'currency': _currencyController.text,
                    });
                    await networkRepository.updateMusicalInstrument(m1);
                    db.updateMusicalInstrument(m1).then((id) {
                        Navigator.pop(context, m1);
                    });
                  }else {
                    MusicalInstrument m1 = MusicalInstrument(_nameController.text, _categoryController.text, _descriptionController.text,
                        int.parse(_quantityOnStockController.text), double.parse(_priceController.text), _currencyController.text);
                    if(ListViewMusicalInstrument.isConnectedToInternet == 1){
                      var id = await networkRepository.saveMusicalInstrument(m1);
                      m1.id = id;
                      m1.isSynchronisedWithServer = 1;
                    } else {
                      m1.id = MusicalInstrumentsDatabaseHelper.useOneId();
                      m1.isSynchronisedWithServer = 0;
                    }
                    db.saveMusicalInstrument(m1).then((id) {
                      m1.id = id;
                          Navigator.pop(context, m1);
                    });
                  }
                }
              },
            ),
          ],
        ),
      ),
    );
  }
}