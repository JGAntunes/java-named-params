#!/bin/bash

docker build --rm --tag pav .

if [ "$RUN" != "" ]; then
  docker run -it --rm pav /bin/bash
fi 
