<template id="user-biometrics-overview">
  <app-layout>
    <div>
      <h3>Biometrics details</h3>
      <ul>
        <li v-for="biometric in biometrics">
         <h4>Biometric details as of {{biometric.recordedon}}</h4>
          <br>
          Age : {{biometric.age}}
          <br>
          Height: {{biometric.height}}
          <br>
          Weight: {{biometric.weight}}
          <br>
          BMI : {{biometric.bmi}}
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-biometrics-overview",{
  template: "#user-biometrics-overview",
  data: () => ({
    biometrics: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/biometric`)
        .then(res => this.biometrics = res.data)
        .catch(() => alert("Error while fetching biometrics"));
  }
});
</script>