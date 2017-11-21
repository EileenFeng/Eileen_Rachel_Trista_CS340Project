#!/bin/bash

make clean
make
java Scheduler $1 $2 $3

