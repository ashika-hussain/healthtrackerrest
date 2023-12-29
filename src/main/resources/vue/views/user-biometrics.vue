<template id="user-biometrics-overview">
  <app-layout>
    <div class="card mb-3" style="background-color: lightgray">
      <div class="card-header">
        <div class="row">
          <h3 class="col-6">
            Biometrics
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
          <div class="col-6"> Add Biometrics </div>
        </div>
      </div>

      <div class="card-body" style="background-color: lightgray">

        <form>
          <div class="form-group">
            <label for="inputAddress">User ID</label>
            <input type="text" class="form-control" id="inputAddress" readonly v-model="userId">
          </div>
          <div class="form-group">
            <label for="biometric">Weight </label>
            <input type="text" class="form-control" v-model="formData.weight" id="weight" placeholder="Weight in kgs">
          </div>


          <div class="form-row">
            <div class="form-group col-md-4">
              <label for="height">Height</label>
              <input type="text" class="form-control" v-model="formData.height" id="height" placeholder="Height in cms">
            </div>
          </div>

          <button @click="addActivity()" class="btn btn-primary">Add Biometrics</button>

        </form>
      </div>
    </div>

    <div class="card mb-3" style="background-color: lightgray">
      <div class="card-header" style="background-color: lightgray">
        <div class="row">
          <div class="col-6"> Biometrics over time  </div>
        </div>
      </div>

      <div class="card-body" v-if="biometrics.length!==0" style="background-color: lightgray">
        <div v-for="biometric in biometrics">
          <div class="card card-body bg-light-gray" style="margin: 15px">
            <span class="float-left">
              BMI  : {{biometric.bmi}}
            </span>

            <div class="col" align="right">
              <button rel="tooltip" title="Delete"
                      class="btn btn-info btn-simple btn-link"
                      @click="deleteActivity(biometric, index)">
                <i class="fas fa-trash" aria-hidden="true"></i>
              </button>
            </div>
            <span> Height : {{biometric.height}} cm</span>
            <span> Weight: {{biometric.weight}} kgs </span>
            <span> Recorded on: {{new Date(biometric.recordedon).toLocaleDateString('en-us', { weekday: "long", year:"numeric", month:"short", day:"numeric"}) }} </span>



          </div>
        </div>

      </div>
      <p style="margin-left: 20px; margin-top: 20px" v-else>No biometrics found! Please add by clicking the create button.</p>
    </div>
    <div class="chart-container"
         style="position: relative; height:40vh; width:100%; border: 1px solid lightgrey; padding: 25px">
      <canvas id="myChart"></canvas>
    </div>


  </app-layout>
</template>

<script>
app.component("user-biometrics-overview",{
  template: "#user-biometrics-overview",
  data: () => ({
    biometrics: [],
    badges: [],
    userId: null,
    hideForm :true,
    formData: [],
    dates : [],
    values : []
  }),
  created() {
    this.userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${this.userId}/biometric`)
        .then(res => {
          this.biometrics = res.data
          if (this.biometrics.length) {
            const ctx = document.getElementById('myChart');
            for (item of this.biometrics) {
              this.dates.push(new Date(item.recordedon).toLocaleDateString('en-us', {
                year: "numeric",
                month: "short",
                day: "numeric"
              }))
              this.values.push(item.bmi)
            }
            this.dates.sort(function (a, b) {
              return new Date(a) - new Date(b);
            });
            new Chart(ctx, {
              type: 'line',
              data: {
                labels: this.dates,
                datasets: [{
                  label: 'BMI',
                  data: this.values,
                  borderWidth: 1
                }]
              },
              options: {
                scales: {
                  y: {
                    beginAtZero: false
                  },
                  x: {
                    grid: {
                      display: false,
                    }
                  },
                  y: {
                    grid: {
                      display: false
                    }
                  },
                },
                plugins: {
                  legend: {
                    display: false
                  }
                }
              }
            })
          }

        })
        .catch(() => console.log("Error while fetching biometrics"));
  },
  methods: {
    addActivity: function (){
      const url = `/api/biometrics`;
      const userId = this.$javalin.pathParams["user-id"];
      axios.post(url,
          {
            weight: this.formData.weight,
            height: this.formData.height,
            recordedon : new Date().toISOString(),
            userId: userId
          })
          .then(response => {
            this.biometrics.push(response.data)
            this.hideForm= true;
          })
          .catch(error => {
            console.log(error)
          })
    },
    deleteActivity: function (biometric, index) {
      if (confirm('Are you sure you want to delete?')) {
        const url = `/api/biometrics/${biometric.id}`;
        axios.delete(url)
            .then(response =>
                this.biometrics.splice(index, 1).push(response.data))
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


</style>