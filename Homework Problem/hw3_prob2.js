let depth = 0;

function main(){
  let num_of_tests = 10000;
  let num_wrong = 0;
  for(let i = 0; i < num_of_tests; i++){
    if(!test_algo(false)){
      num_wrong++;
    }
  }
  console.log("Number of incorrect algo runs: " + num_wrong);
}

function test_algo(console_debug){
  let arr = generateRandomArray();
  let goal_num = arr.splice(getRandomInt(arr.length), 1);
  depth = 0;
  let result = my_function(arr, 0, arr.length - 1);
  if(console_debug){
    console.log("Goal number: " + goal_num);
    console.log("Returned number: " + result);
    console.log("Recursive Depth: " + depth);
    console.log("Algo correct? " + (goal_num == result));
    console.log();
  }
  return (goal_num == result);
}

function my_function(arr, start, end){
  depth++;
  if(start == end){
    return arr[start];
  }
  let index = Math.floor((end - start) / 2) + start;
  if(arr[index] == arr[index + 1]){
    if((end - index + 1) % 2 == 0){
      return my_function(arr, start, index - 1);
    }
    else{
      return my_function(arr, index + 2, end);
    }
  }
  else if(arr[index] == arr[index - 1]){
    if((index - start + 1) % 2 == 0){
      return my_function(arr, index + 1, end);
    }
    else{
      return my_function(arr, start, index - 2);
    }
  }
  else{
    return arr[index];
  }
}

function generateRandomArray(){
  let length = getRandomInt(1000) + 1; //half of the length of the array, must be at least 1
  let array = [];
  let new_num = getRandomInt(10000);
  array.push(new_num);
  array.push(new_num);
  for(let i = 0; i < length; i++){
    new_num = getRandomInt(10000) + array[array.length - 1] + 1; //must be at least 1 greater than previous numbers
    array.push(new_num);
    array.push(new_num);
  }

  return array;
}

function getRandomInt(max) {
  return Math.floor(Math.random() * Math.floor(max));
}
