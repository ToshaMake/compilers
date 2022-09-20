fn factorial(n : i32) -> i32 {
    let i: i32 = 1;
    let result: i32 = 1;
    while i < n {
        result = result * i;
        i = i + 1;
    }
    return result;
}

fn main() -> i32 {
    let n : i32 = 0;
    read_line(n);
    let f : i32 = factorial(n);
    println!("{}\n", f);
    return 0;
}