<template id="user-profile">
  <app-layout>
    <div v-if="noUserFound">
      <p> We're sorry, we were not able to retrieve this user.</p>
      <p> View <a :href="'/users'">all users</a>.</p>
    </div>

    <div class="card bg-light mb-3" v-if="!noUserFound">

      <div class="card-header" style="background-color: lightgray">

        <div class="row" style="background-color: lightgray">
          <div class="col-2" style="color: black; font-weight: 600"> User Profile </div>
          <div class="col-7" style=" background-color: lightgray">
            <div class="list-group list-group-flush" style="background-color: lightgray">
              <div class="list-group-item d-flex align-items-start" style="background-color: lightgray">

                <div class="p-2">
                  <span> <a :href="`/users/${user.id}/activities`" type="button" class="btn btn-secondary" style="margin-right: 15px; cursor: pointer; background-color: #007BFF; color: white; display: block; margin-bottom: 5px;">Activities</a></span>
                </div>

                <div class="p-2">
                  <span> <a :href="`/users/${user.id}/biometrics`" type="button" class="btn btn-secondary" style="margin-right: 15px; cursor: pointer; background-color: #007BFF; color: white; display: block; margin-bottom: 5px;">Biometrics</a></span>
                </div>

                <div class="p-2">
                  <span> <a :href="`/users/${user.id}/goals`" type="button"  class="btn btn-secondary" style="margin-right: 15px; cursor: pointer; background-color: #007BFF; color: white; display: block; margin-bottom: 5px;">Goals</a></span>
                </div>

                <div class="p-2">
                  <a :href="`/users/${user.id}/calories`" type="button"  class="btn btn-secondary" style="margin-right: 15px; cursor: pointer; background-color: #007BFF; color: white; display: block; margin-bottom: 5px;">Calorie Intake</a>
                </div>

              </div>
            </div>
          </div>

          <div class="col" align="right">
            <button rel="tooltip" title="Update"
                    class="btn btn-info btn-simple btn-link"
                    @click="updateUser()">
              <i class="far fa-save" aria-hidden="true"></i>
            </button>

            <button rel="tooltip" title="Delete"
                    class="btn btn-info btn-simple btn-link"
                    @click="deleteUser()">
              <i class="fas fa-trash" aria-hidden="true"></i>
            </button>

          </div>
        </div>

      </div>
      <div class="card-body">
        <form>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-id">User ID</span>
            </div>
            <input type="number" class="form-control" v-model="user.id" name="id" readonly placeholder="Id"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-name">Name</span>
            </div>
            <input type="text" class="form-control" v-model="user.name" name="name" placeholder="Name"/>
          </div>
          <div class="input-group mb-3">
            <div class="input-group-prepend">
              <span class="input-group-text" id="input-user-email">Email</span>
            </div>
            <input type="email" class="form-control" v-model="user.email" name="email" placeholder="Email"/>
          </div>
        </form>
        <div class="medal-icon">
          <div class="decorative-line"></div>
          {{levels.level}}
        </div>
      </div>
      <div class="card-footer text-left">
      </div>
    </div>






  </app-layout>
</template>

<style>

</style>
<script>
app.component("user-profile", {
  template: "#user-profile",
  data: () => ({
    user: null,
    noUserFound: false,
    activities: [],
    levels:{}
  }),
  created: function () {
    const userId = this.$javalin.pathParams["user-id"];
    const url = `/api/users/${userId}`
    axios.get(url)
        .then(res => this.user = res.data)
        .catch(error => {
          console.log("No user found for id passed in the path parameter: " + error)
          this.noUserFound = true
        })
    axios.get(`/api/users/${userId}/levels`)
        .then(res => this.levels = res.data[0])
        .catch(error => {
          console.log(error)
        })
  },
  methods: {
    updateUser: function () {
      const userId = this.$javalin.pathParams["user-id"];
      const url = `/api/users/${userId}`
      axios.patch(url,
          {
            name: this.user.name,
            email: this.user.email
          })
          .then(response =>
              this.user.push(response.data))
          .catch(error => {
            console.log(error)
          })
      alert("User updated!")
    },
    deleteUser: function () {
      if (confirm("Do you really want to delete?")) {
        const userId = this.$javalin.pathParams["user-id"];
        const url = `/api/users/${userId}`
        axios.delete(url)
            .then(response => {
              alert("User deleted")
              //display the /users endpoint
              window.location.href = '/users';
            })
            .catch(function (error) {
              console.log(error)
            });
      }
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

.medal-icon {
  position: relative;
  width: 60px;
  height: 100px;
  border: 2px solid brown;
  border-radius: 7px;
  background-color: #ffe042;
  transform: rotate(-15deg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5em;
  font-weight: bold;
  color: brown;
}

.medal-icon::before {
  content: '';
  position: absolute;
  top: -10px;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 0;
  border-style: solid;
  border-width: 0 20px 30px 20px;
  border-color: transparent transparent #ffe042 transparent;
}

.decorative-line {
  position: absolute;
  top: 5px;
  left: 50%;
  transform: translateX(-50%);
  width: 30px;
  height: 2px;
  background-color: brown;
}
</style>