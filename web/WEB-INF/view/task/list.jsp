<h1 class="title">Tasks list</h1>
<table class="entry">
<c:forEach var="task" items="${tasks.tasks}">
    <tr>
        <td class="task">${task.key}</td>
        <td>${task.value.size}</td>
    </tr>
    <c:forEach var="factory" items="${task.value.factories}">
        <tr>
            <td class="factory">${factory.key}</td>
            <td>${factory.value}</td>
        </tr>
    </c:forEach>
</c:forEach>
</table>