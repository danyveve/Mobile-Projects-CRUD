import 'package:flutter/material.dart';
import 'package:lab1_flutter/ui/listview_musical_instrument.dart';

void main() => runApp(
  MaterialApp(
    title: 'Lab1 FLUTTER',
    theme: ThemeData(
      primarySwatch: Colors.myColor,
    ),
    home: ListViewMusicalInstrument(),
  ),
);