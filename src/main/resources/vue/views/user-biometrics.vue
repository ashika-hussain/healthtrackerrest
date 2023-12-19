<template id="user-activity-overview">
  <app-layout>
    <div>
      <h3>Activities list </h3>
      <ul>
        <li v-for="activity in biometrics">
          {{biometrics.id}}: {{biometrics.height}}
        </li>
      </ul>
    </div>
  </app-layout>
</template>

<script>
app.component("user-biometrics-overview",{
  template: "#user-biometrics-overview",
  data: () => ({
    activities: [],
  }),
  created() {
    const userId = this.$javalin.pathParams["user-id"];
    axios.get(`/api/users/${userId}/biometrics`)
        .then(res => this.activities = res.data)
        .catch(() => alert("Error while fetching biometrics"));
  }
});
</script>