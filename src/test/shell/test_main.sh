testDir=`pwd`

cd ../../..

rm -f out err

mvn -q exec:java -Dexec.mainClass="us.remple.Crawler" >out 2>err
if test $? -ne 1
then echo "no args test failed - wrong exit code"
fi

if test -s out
then echo "no args test failed - unexpected stdout:" ; cat out
fi

diff err ${testDir}/args-error
if test $? -ne 0
then echo "no args test failed - wrong stderr:" ; cat err
fi

run_test() {
  rm -f out err

  mvn -q exec:java -Dexec.mainClass="us.remple.Crawler" -Dexec.args="${1}" >out 2>err
  if test $? -ne $3
  then echo $2 "test failed - wrong exit code"
  fi

  if test -s out
  then echo $2 "test failed - unexpected stdout:" ; cat out
  fi

  diff err ${testDir}/${4}
  if test $? -ne 0
  then echo $2 "test failed - wrong stderr:" ; cat err
  fi
}

run_test "" "empty args" 1 args-error

run_test "junk junk2" "too many args" 1 args-error

run_test "farfle" "invalid url" 2 invalid-url

run_test "http://invaliddomain.com" "invalid domain" 3 invalid-domain

rm -f out err

