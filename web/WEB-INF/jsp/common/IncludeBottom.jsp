</div>

<div id="Footer">

    <div id="PoweredBy">&nbsp<a href="https://www.csu.edu.cn/">www.csu.edu.cn</a>
    </div>

    <div id="Banner"><c:if test="${sessionScope.accountBean != null }">
        <c:if test="${sessionScope.accountBean.account.bannerOption}">
            ${sessionScope.accountBean.account.bannerName}
        </c:if>
    </c:if>

    </div>

</div>

</body>
</html>
