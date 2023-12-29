<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn">Click here.</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Activities</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn">Click here.</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Total Biometrics</h5>
          <div class="card-body">
            <h5 class="card-title">{{biometrics.length}} biometrics</h5>
            <a href="/biometrics" class="btn">Click here.</a>
          </div>
        </div>
      </div>
    </div>
  </app-layout>
</template>

<script>
app.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: []
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/biometrics")
            .then(res => this.biometrics = res.data)
            .catch(() => alert("Error while fetching biometrics"));
      }
    });
</script>

<style>
body {
  font-family: 'Arial', sans-serif;
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

i,button {
  width: 4em;
  padding: 10px;
  background-color: antiquewhite;
  color: #7689de;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1em;
}
a {
  padding: 10px;
  background-color: white;
  color: darkblue;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1em;
}


</style>