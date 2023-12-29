<template id="user-calorie-intake">
  <app-layout>
    <div class="card bg-light mb-3">
      <div class="card-header">
        <div class="row">
          <h3 class="col-6">
            Calorie Intakes
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


    <div class="card bg-light mb-3" :class="{ 'd-none': hideForm}">
      <div class="card-header">
        <div class="row">
          <div class="col-6"> Add Calorie Intake </div>
        </div>
      </div>

      <div class="card-body">

        <form>
          <div class="form-group">
            <label for="inputAddress">User ID</label>
            <input type="text" class="form-control" id="inputAddress" readonly v-model="userId">
          </div>
          <div class="form-group">
            <label for="activity">Meal Type</label>
            <select class="form-select" id="activity" v-model="formData.mealType" style="min-width: 200px; min-height: 30px; margin-left: 8px">
              <option selected value="Breakfast">Breakfast</option>
              <option value="Lunch">Lunch</option>
              <option value="Dinner">Snack</option>
              <option value="Dinner">Dinner</option>
            </select>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6">
              <label for="food">Food</label>
              <input type="text" class="form-control" v-model="formData.food" id="food">
            </div>

            <div class="form-group col-md-6">
              <label for="calorie">Calorie</label>
              <input type="text" class="form-control" id="calorie" v-model="formData.calorie" placeholder="Estimated calorie intake...">
            </div>
          </div>
          <div class="form-row">
            <div class="form-group col-md-6">
              <label for="inputCal">Number of units</label>
              <input type="text" class="form-control" id="unit" v-model="formData.number">
            </div>
          </div>
          <button @click="addData()" class="btn btn-primary">Add</button>

        </form>
      </div>
    </div>

    <div class="card mb-3" style="background-color: lightgray">
      <div class="card-header" style="background-color: lightgray">
        <div class="row">
          <div class="col-6"> Calorie Intake Log</div>
        </div>
      </div>

      <div class="card-body" v-if="calories.length!==0" style="background-color: lightgray">
        <div v-for="calorie in calories">
          <div class="card card-body bg-light-gray" style="margin: 15px">
            <span class="float-left">
              Had {{calorie.number}} {{calorie.food}} (s) as {{calorie.mealType}}
            </span>

            <div class="col" align="right">
              <button rel="tooltip" title="Delete"
                      class="btn btn-info btn-simple btn-link"
                      @click="deleteActivity(activity, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>

            <span>  Total Calorie : {{calorie.calorie}}</span>

          </div>
        </div>

      </div>
      <p style="margin-left: 20px; margin-top: 20px" v-else>No calorie log found! Please add by clicking the create button.</p>
    </div>
  </app-layout>
</template>
<script>

app.component("user-calorie-intake", {
  template: "#user-calorie-intake",
  data: () => ({
    calories: [],
    userId: null,
    hideForm :true,
    formData: [],
  }),
  created() {
    this.userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${this.userId}/calories`)
        .then(res => this.calories = res.data)
        .catch(() => console.log("Error while fetching calorie intake"))

  },
  methods: {
    addData: function () {
      const url = `/api/calorie`;
      const userId = this.$javalin.pathParams["user-id"];
      axios.post(url,
          {
            mealType : this.formData.mealType,
            recordedon : new Date().toISOString(),
            food: this.formData.food,
            calorie : this.formData.calorie,
            number : this.formData.number,
            userId: userId
          })
          .then(response => {
            this.calories.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteLog: function (calorie, index) {
      if (confirm('Are you sure you want to delete?')) {
        const url = `/api/intakes/${calorie.id}`;
        axios.delete(url)
            .then(response =>
                this.calories.splice(index, 1).push(response.data))
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