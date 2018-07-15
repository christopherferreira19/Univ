var a = 5;
var b : I32 | Boolean;
val c = 1;

fn incr(v: I32) { return v + c; }
fn decr(v: I32) { return v - c; }

if a == 5 {
    a = decr(a);
    b = true;
}
else {
    a = incr(a);
    b = 100;
}

while (b and a > 0) {
    print(a);
    a = decr(a);
}
