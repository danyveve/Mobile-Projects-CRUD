import 'dart:async';

import 'package:flutter/material.dart';
import 'package:lab1_flutter/model/musical_instrument.dart';
import 'package:lab1_flutter/util/musical_instruments_database_helper.dart';
import 'package:lab1_flutter/util/network_repository.dart';

import 'musical_instrument_screen.dart';
import 'package:connectivity/connectivity.dart';

class ListViewMusicalInstrument extends StatefulWidget {
  static int isConnectedToInternet = 0;

  @override
  _ListViewMusicalInstrumentState createState() =>
      new _ListViewMusicalInstrumentState();
}

class _ListViewMusicalInstrumentState extends State<ListViewMusicalInstrument> {
  List<MusicalInstrument> items = new List();
  MusicalInstrumentsDatabaseHelper db = new MusicalInstrumentsDatabaseHelper();
  NetworkRepository networkRepository = new NetworkRepository();
  StreamSubscription subscription;

  @override
  void initState() {
    super.initState();

    subscription = Connectivity()
        .onConnectivityChanged
        .listen((ConnectivityResult result) {
      // Got a new connectivity status!
      if (result == ConnectivityResult.none) {
        ListViewMusicalInstrument.isConnectedToInternet = 0;
      } else {
        ListViewMusicalInstrument.isConnectedToInternet = 1;
      }
      fetchData();
    });

    fetchData();
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Lab1 FLUTTER',
      home: Scaffold(
        appBar: AppBar(
          title: Text('Lab1 FLUTTER'),
          backgroundColor: Colors.myColor,
          centerTitle: false,
        ),
        body: Center(
          child: ListView.builder(
              itemCount: items.length * 2,
              itemBuilder: (context, position) {
                if (position.isOdd) return Divider();

                final index = position ~/ 2;

                return _buildRow(items[index], index, context);
              }),
        ),
        floatingActionButton: Builder(
          builder: (context) => Column(
            crossAxisAlignment: CrossAxisAlignment.center,
            mainAxisSize: MainAxisSize.max,
            mainAxisAlignment: MainAxisAlignment.end,
            children: <Widget>[
              FloatingActionButton(
                child: Image.asset("assets/ic_add.png"),
                backgroundColor: Colors.myColor[50],
                onPressed: () => _createNewMusicalInstrument(context),
                heroTag: 0,
              ),
              Padding(
                padding: EdgeInsets.all(8.0),
              ),
              FloatingActionButton(
                child: Image.asset("assets/ic_refresh.png"),
                backgroundColor: Colors.myColor[50],
                onPressed: () => fetchData(),
                heroTag: 1,
              )
            ],
          ),
        ),
      ),
    );
  }

  Widget _buildRow(MusicalInstrument musicalInstrument, position, context) {
    return Row(
      children: <Widget>[
        Expanded(
          flex: 5,
          child: Column(
            children: <Widget>[
              Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  '${musicalInstrument.name}',
                  textAlign: TextAlign.left,
                  style: TextStyle(
                    fontSize: 22.0,
                    color: Colors.myColor,
                  ),
                ),
              ),
              Align(
                alignment: Alignment.centerLeft,
                child: Text(
                  '${musicalInstrument.description}',
                  style: new TextStyle(
                    fontSize: 18.0,
                    fontStyle: FontStyle.italic,
                  ),
                ),
              ),
            ],
          ),
        ),
        Expanded(
            flex: 4,
            child: Column(
              children: <Widget>[
                Text(
                  '${musicalInstrument.price.toString() + " " + musicalInstrument.currency}',
                  textAlign: TextAlign.center,
                  style: TextStyle(
                    fontSize: 22.0,
                    color: Colors.myColor[50],
                  ),
                )
              ],
            )),
        Expanded(
            flex: 1,
            child: Column(
              children: <Widget>[
                IconButton(
                  icon: Icon(Icons.edit),
                  onPressed: () =>
                      _navigateToMusicalInstrument(context, musicalInstrument),
                ),
                IconButton(
                  icon: Icon(Icons.delete),
                  onPressed: () => _deleteMusicalInstrument(
                      context, musicalInstrument, position),
                ),
              ],
            )),
      ],
    );
  }

  void _deleteMusicalInstrument(BuildContext context,
      MusicalInstrument musicalInstrument, int position) async {
    if(ListViewMusicalInstrument.isConnectedToInternet == 0){
      printSnackBarMessage(context, "This action is not supported in offline mode!");
      return ;
    }
    try {
      await networkRepository.deleteMusicalInstrument(musicalInstrument.id);
      db.deleteMusicalInstrument(musicalInstrument.id).then((musicalInstruments) {
        setState(() {
          items.removeAt(position);
        });
      });
    } catch (e){
      printSnackBarMessage(context, "Delete operation failed!");
    }
  }

  void _navigateToMusicalInstrument(
      BuildContext context, MusicalInstrument musicalInstrument) async {

    if(ListViewMusicalInstrument.isConnectedToInternet == 0){
      printSnackBarMessage(context, "This action is not supported in offline mode!");
      return ;
    }

    try{
      MusicalInstrument result = await Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => MusicalInstrumentScreen(musicalInstrument)),
      );
      if (result == null || result.id == null) {
        printSnackBarMessage(context, "Update not performed! Missing info!");
      } else {
        setState(() {
          for (int i = 0; i < items.length; i = i + 1) {
            if (items[i].id == result.id) {
              items.removeAt(i);
              items.insert(i, result);
              break;
            }
          }
        });
      }
    } catch (e){
      printSnackBarMessage(context, "Update failed!");
    }
  }

  void _createNewMusicalInstrument(BuildContext context) async {

    try{
      MusicalInstrument result = await Navigator.push(
        context,
        MaterialPageRoute(
            builder: (context) => MusicalInstrumentScreen(
                MusicalInstrument('', '', '', 0, 0.0, ''))),
      );

      if (result == null || result.id == null) {
        printSnackBarMessage(context, "Update not performed! Missing info!");
      } else {
        setState(() {
          items.add(result);
        });
      }
    } catch (e){
      printSnackBarMessage(context, "Save failed!");
    }
  }

  void fetchData() async {
    if (ListViewMusicalInstrument.isConnectedToInternet == 1) {
      try {
        //insert from local db the ones that were not synchronised
        items.forEach((localMusicalInstrument) async {
          if (localMusicalInstrument.isSynchronisedWithServer == 0) {
            await networkRepository.saveMusicalInstrument(localMusicalInstrument);
          }
        });

        //reload from server
        List<MusicalInstrument> musicalInstrumentsFromServer =
            await networkRepository.getAllMusicalInstruments();
        await db.deleteAll();
        musicalInstrumentsFromServer.forEach((musicalInstrument) {
          db.saveMusicalInstrument(musicalInstrument);
        });
        setState((){
          items.clear();
          items.addAll(musicalInstrumentsFromServer);
        });
      } catch (e) {
        printSnackBarMessage(context, "Could not fetch data from server!");
        fetchFromDb();
      }
    } else {
      fetchFromDb();
    }
  }

  void fetchFromDb() {
    db.getAllMusicalInstruments().then((musicalInstruments) {
      setState(() {
        items.clear();
        musicalInstruments.forEach((musicalInstrument) {
          items.add(MusicalInstrument.fromMap(musicalInstrument));
        });
      });
    });
  }

  void printSnackBarMessage(BuildContext context, String text){
    Scaffold.of(context)
      ..removeCurrentSnackBar()
      ..showSnackBar(
          SnackBar(content: Text(text)));
  }

  @override
  dispose() {
    super.dispose();
    subscription.cancel();
  }
}
