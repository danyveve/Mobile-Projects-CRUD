import 'dart:async';

import 'package:lab1_flutter/model/musical_instrument.dart';
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

class MusicalInstrumentsDatabaseHelper {
  static final MusicalInstrumentsDatabaseHelper _instance =
      new MusicalInstrumentsDatabaseHelper.internal();

  factory MusicalInstrumentsDatabaseHelper() => _instance;

  final String tableMusicalInstruments = 'musical_instruments_table';
  final String columnId = 'id';
  final String columnName = 'name';
  final String columnCategory = 'category';
  final String columnDescription = 'description';
  final String columnQuantityOnStock = 'quantityOnStock';
  final String columnPrice = 'price';
  final String columnCurrency = 'currency';
  final String isSynchronisedWithServer = 'isSynchronisedWithServer';
  static int id = 0;

  static int useOneId(){
    id -= 1;
    return id;
  }

  static Database _db;

  MusicalInstrumentsDatabaseHelper.internal();

  Future<Database> get db async {
    if (_db != null) {
      return _db;
    }
    _db = await initDb();

    return _db;
  }

  initDb() async {
    String databasesPath = await getDatabasesPath();
    String path = join(databasesPath, 'musical_instruments_db.db');

    var db = await openDatabase(path, version: 1, onCreate: _onCreate);
    return db;
  }

  void _onCreate(Database db, int newVersion) async {
    await db.execute(
        'CREATE TABLE $tableMusicalInstruments($columnId INTEGER PRIMARY KEY, $columnName TEXT, '
            '$columnCategory TEXT, $columnDescription TEXT, $columnQuantityOnStock INTEGER, '
            '$columnPrice DOUBLE, $columnCurrency TEXT, $isSynchronisedWithServer INTEGER)');
  }

  Future<int> saveMusicalInstrument(MusicalInstrument musicalInstrument) async {
    var dbClient = await db;
    var result = await dbClient.insert(tableMusicalInstruments, musicalInstrument.toMap());

    return result;
  }

  Future<List> getAllMusicalInstruments() async {
    var dbClient = await db;
    var result = await dbClient
        .query(tableMusicalInstruments, columns: [columnId, columnName, columnCategory,
                                                    columnDescription, columnQuantityOnStock, columnPrice, columnCurrency, isSynchronisedWithServer]);

    return result.toList();
  }

  Future<int> getCount() async {
    var dbClient = await db;
    return Sqflite.firstIntValue(
        await dbClient.rawQuery('SELECT COUNT(*) FROM $tableMusicalInstruments'));
  }

  Future<MusicalInstrument> getMusicalInstrument(int id) async {
    var dbClient = await db;
    List<Map> result = await dbClient.query(tableMusicalInstruments,
        columns: [columnId, columnName, columnCategory,
          columnDescription, columnQuantityOnStock, columnPrice, columnCurrency, isSynchronisedWithServer],
        where: '$columnId = ?',
        whereArgs: [id]);

    if (result.length > 0) {
      return new MusicalInstrument.fromMap(result.first);
    }

    return null;
  }

  Future<int> deleteMusicalInstrument(int id) async {
    var dbClient = await db;
    return await dbClient
        .delete(tableMusicalInstruments, where: '$columnId = ?', whereArgs: [id]);
  }

  Future<int> updateMusicalInstrument(MusicalInstrument musicalInstrument) async {
    var dbClient = await db;
    return await dbClient.update(tableMusicalInstruments, musicalInstrument.toMap(),
        where: "$columnId = ?", whereArgs: [musicalInstrument.id]);
}

  Future<int> deleteAll() async{
    var dbClient = await db;
    return await dbClient.delete(tableMusicalInstruments, where: "1");
  }

  Future close() async {
    var dbClient = await db;
    return dbClient.close();
  }
}
