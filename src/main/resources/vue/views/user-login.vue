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