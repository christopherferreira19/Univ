var a: Integer;
var b = 4;

a = 5;
b = 6;

while a > 0 do
    if a < b then
        var c = 1;
        a = a - c;
        b = b + c;
    else
        var c = 3;
        a = a + c;
        b = b - c;
    end
end

print(b);