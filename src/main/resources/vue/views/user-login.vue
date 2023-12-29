<template id="user-login">
  <div class="card bg-light mb-3">
    <div class="card-header">
    <h2>Login</h2>
    </div>
    <div class="card-body">
    <form id="login-card">
      <div class="row input-group mb-3">
      <label for="username">Username:</label>
      <input type="text" id="username" v-model="formData.username" required />
      </div>
      <div class="row input-group mb-3">
      <label for="password">Password:</label>
      <input type="password" id="password" v-model="formData.password" required />
      </div>
      <div class="row input-group mb-3">
      <button @click.prevent="login()">Login</button>
      </div>
    </form>
    </div>
  </div>
</template>

<script>
app.component("user-login",{
  template: "#user-login",
  data: () => ({
    logindetails : [],
    formData: []
  }),
  methods: {
     login(){
       console.log(this.formData);
      axios.post(`/api/login`,
          {
            username: this.formData.username,
            password: this.formData.password
          })
          .then(res => {
            this.logindetails = res.data;
            const userid = this.logindetails['userId']
            if(this.logindetails['role'] == 'admin')
              window.location.href = '/';
            else
              window.location.href = `/users/${userid}`;
          })
          .catch(error => {
            alert("Invalid Credentials")
          })

    }
  }

});
</script>
<style>
body {
  font-family: 'Arial', sans-serif;
  background-color: #a9dce3;
  margin: 0;
  padding: 0;
}

.card {
  background-color: white;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 8px;
}

.card-header {
  background-color: #7689de;
  color: white;
  padding: 15px;
  font-weight: 600;
  font-size: 1.2em;
}

.card-body {
  padding: 20px;
  background-color: #7689aa;
}

.card-footer {
  background-color: #7689de;
  color: white;
  text-align: center;
  padding: 10px;
}

input {
  width: 100%;
  padding: 10px;
  margin-bottom: 15px;
  box-sizing: border-box;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-size: 1em;
}

i {
  width: 4em;
  padding: 10px;
  background-color: antiquewhite;
  color: #7689de;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1em;
}


</style>