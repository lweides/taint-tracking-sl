

function main() {
  // both tainted
  t1 = taint("hello");
  t2 = taint(" world");
  println(isTainted(t1));
  println(isTainted(t2));
  println(t1);
  println(t2);
  t3 = t1 + t2;
  println(isTainted(t3));
  println(t3);
  println("");

  // first tainted
  t1 = taint("hello");
  t2 = " world";
  println(isTainted(t1));
  println(isTainted(t2));
  println(t1);
  println(t2);
  t3 = t1 + t2;
  println(isTainted(t3));
  println(t3);
  println("");

  // second tainted
  t1 = "hello";
  t2 = taint(" world");
  println(isTainted(t1));
  println(isTainted(t2));
  println(t1);
  println(t2);
  t3 = t1 + t2;
  println(isTainted(t3));
  println(t3);
  println("");

  // first tainted number
  t1 = taint("hello");
  t2 = 123;
  println(isTainted(t1));
  println(t1);
  println(t2);
  t3 = t1 + t2;
  println(isTainted(t3));
  println(t3);
  println("");
  
  // second tainted number
  t1 = 123;
  t2 = taint(" world");
  println(isTainted(t2));
  println(t1);
  println(t2);
  t3 = t1 + t2;
  println(isTainted(t3));
  println(t3);
}
