#!/bin/bash

./gradlew :rust:run --args="$1 $3"


dir=$(dirname */$1)
name="rust/$1"
name=${name##$dir/}
name=${name%%.rs}

llfile="rust/code/$name.ll"
bcfile="rust/code/$name.bc"
file="$2"

llvm-as $llfile -o $bcfile
clang -o $file $bcfile

rm -rf rust/code/*
