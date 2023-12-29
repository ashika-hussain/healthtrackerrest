<template id="user-activity-overview">
  <app-layout>
    <div class="card mb-3" style="background-color: lightgray">
      <div class="card-header">
        <div class="row">
          <h3 class="col-6">
            Activities
          </h3>
          <div class="col" align="right">
            <button rel="tooltip" title="Add"
                    class="btn btn-info btn-simple btn-link"
                    @click="hideForm =!hideForm">
              <i class="fa fa-plus" aria-hidden="true"></i>
            </button>
          </div>
        </div>
      </div>
    </div>


    <div class="card mb-3" :class="{ 'd-none': hideForm}" style="background-color: lightgray">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Add Activity </div>
        </div>
      </div>

      <div class="card-body" style="background-color: lightgray">

        <form>
          <div class="form-group">
            <label for="inputAddress">User ID</label>
            <input type="text" class="form-control" id="inputAddress" readonly v-model="userId">
          </div>
          <div class="form-group">
            <label for="activity">Activity </label>
            <select class="form-select" id="activity" v-model="formData.description" style="min-width: 200px; min-height: 30px; margin-left: 8px"  >
              <option selected value="Walking">Walking</option>
              <option value="Cycling">Jogging</option>
              <option value="Running">Marathon</option>
              <option value="Climbing">Cycling</option>
              <option value="Climbing">Zumba</option>
            </select>
          </div>


          <div class="form-row">
            <div class="form-group col-md-4">
              <label for="inputTime">Duration</label>
              <input type="text" class="form-control" v-model="formData.duration" id="inputTime" placeholder="Add duration in minutes">
            </div>

            <div class="form-group col-md-4">
              <label for="inputCal">Calories</label>
              <input type="text" class="form-control" id="inputCal" v-model="formData.calories" placeholder="Add calories burned in calories">
            </div>
          </div>

          <button @click="addActivity()" class="btn btn-primary">Add Activity</button>

        </form>
      </div>
    </div>

    <div class="card mb-3" style="background-color: lightgray">
      <div class="card-header" style="background-color: lightgray">
        <div class="row">
          <div class="col-6"> Activities List </div>
        </div>
      </div>

      <div class="card-body" v-if="activities.length!==0" style="background-color: lightgray">
        <div v-for="activity in activities">
          <div class="card card-body bg-light-gray" style="margin: 15px">
            <h5 class="float-left">
              Activity : {{activity.description}}
            </h5>

            <div class="col" align="right">
              <button rel="tooltip" title="Delete"
                      class="btn btn-info btn-simple btn-link"
                      @click="deleteActivity(activity, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>
            <span> Duration : {{activity.duration}}</span>
            <span> Calories burned : {{activity.calories}} </span>
            <span> Started at {{activity.started}} </span>



          </div>
        </div>

      </div>
      <p style="margin-left: 20px; margin-top: 20px" v-else>No activities found! Please add by clicking the create button.</p>
    </div>

  </app-layout>
</template>

<script>
app.component("user-activity-overview",{
  template: "#user-activity-overview",
  data: () => ({
    activities: [],
    badges: [],
    userId: null,
    hideForm :true,
    formData: []
  }),
  created() {
    this.userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${this.userId}/activities`)
        .then(res => this.activities = res.data)
        .catch(() => console.log("Error while fetching activities"));
  },
  methods: {
    addActivity: function (){
      const url = `/api/activities`;
      const userId = this.$javalin.pathParams["user-id"];
      axios.post(url,
          {
            description: this.formData.description,
            duration: this.formData.duration,
            distance: this.formData.distance,
            calories: this.formData.calories,
            started: new Date().toISOString(),
            userId: userId
          })
          .then(response => {
            this.activities.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteActivity: function (activity, index) {
      if (confirm('Are you sure you want to delete?')) {
        const url = `/api/activities/${activity.id}`;
        axios.delete(url)
            .then(response =>
                this.activities.splice(index, 1).push(response.data))
            .catch(function (error) {
              console.log(error)
            });
      }
    },
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

i , button{
  width: max-content;
  padding: 10px;
  background-color: antiquewhite;
  color: #7689de;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1em;
}


</style>