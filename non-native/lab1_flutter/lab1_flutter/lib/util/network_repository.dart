import 'dart:convert';

import 'package:http/http.dart';
import 'package:lab1_flutter/model/musical_instrument.dart';

class NetworkRepository{
  Future<List<MusicalInstrument>> getAllMusicalInstruments() async{
    String url = "http://10.0.2.2:8080/musicalInstrumentsApi/getAll";
    Response response = await get(url).timeout(new Duration(seconds: 5));

    int statusCode = response.statusCode;
    if(statusCode != 200){
      throw new Exception("Retrieving instruments from server failed");
    }

    String jsonList = response.body;
    List<dynamic> decodedJsonList = json.decode(jsonList);

    List<MusicalInstrument> list = List<MusicalInstrument>();
    decodedJsonList.forEach((instrument) {
      list.add(MusicalInstrument.fromJson(instrument));
    });
    return list;
  }

  Future<int> saveMusicalInstrument(MusicalInstrument musicalInstrument) async{
    String url = "http://10.0.2.2:8080/musicalInstrumentsApi/save";
    Map<String, String> headers = {"Content-type": "application/json"};
    String jsonString = json.encode(musicalInstrument.toJson());
    Response response = await post(url, headers: headers, body: jsonString);

    int statusCode = response.statusCode;
    if(statusCode != 200){
      throw new Exception("Saving instrument to server failed");
    }
    String body = response.body;

    return int.parse(body);
  }

  Future<MusicalInstrument> updateMusicalInstrument(MusicalInstrument musicalInstrument) async{
    String url = "http://10.0.2.2:8080/musicalInstrumentsApi/update";
    Map<String, String> headers = {"Content-type": "application/json"};
    String jsonString = json.encode(musicalInstrument.toJson());
    Response response = await put(url, headers: headers, body: jsonString);

    int statusCode = response.statusCode;
    if(statusCode != 200){
      throw new Exception("Updating instrument to server failed");
    }
    String body = response.body;

    return MusicalInstrument.fromJson(json.decode(body));
  }

  Future<void> deleteMusicalInstrument(int id) async{
    // post 1
    String url = "http://10.0.2.2:8080/musicalInstrumentsApi/delete/" + id.toString();
    Response response = await delete(url);

    int statusCode = response.statusCode;
    if(statusCode != 200){
      throw new Exception("Deleting instrument to server failed");
    }
  }
}