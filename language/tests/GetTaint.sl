function main() {
  t = taint("hello", "world");
  o1 = getTaint(t, 0);
  println(o1);
  t1 = "hello";
  o2 = getTaint(t1, 0);
  println(o2);
}