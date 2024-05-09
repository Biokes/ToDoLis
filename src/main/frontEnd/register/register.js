const login= document.forms["login"]
const register = document.forms["register"]
login.addEventListener('submit',(e)=>{
    e.preventDefault();
    console.log({"username":document.getElementById("username")})
    console.log({"password":document.getElementById("password")})
    console.log({"confirm password":document.getElementById("confirm password")})
})