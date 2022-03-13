function main() {
  t1 = add(taint("1"), "2");
  println(isTainted(t1));

  t2 = add(taint("1"), taint("2"));
  println(isTainted(t2));

  t3 = add(taint("1"), 2);
  println(isTainted(t3));

  t4 = add(1, taint("2"));
  println(isTainted(t4));

  t5 = add(1, "2");
  println(isTainted(t5));

  obj = new();
  obj.i = 3;
  t6 = add(obj, "2");
  println(isTainted(t6));

  t7 = add(obj, taint("2"));
  println(isTainted(t7));

  obj.t = taint("123");
  println(isTainted(obj.t));
  t8 = add(obj.t, obj.i);
  println(isTainted(t8));
}

function add(a, b) {
  return a + b;
}