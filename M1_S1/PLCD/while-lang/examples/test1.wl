var a: Integer;
var b = 4;

a = 5;
b = 6;

if a < b then
    var c = 1;
    a = a - c;
    b = b + c;
else
    var c = 3;
    a = a + c;
    b = b - 2 * c;
end

while a > 0 do
    b = b + 2;
    a = a - 1;
end

print(b);